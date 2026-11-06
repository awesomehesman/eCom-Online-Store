import type { ProductSummary, ProductSummaryMedia } from './product-summary';

const validated = Symbol('validated catalogue page');
type RecordValue = Record<string, unknown>;
export type CatalogueSize =
  | { readonly kind: 'APPAREL'; readonly code: string; readonly label: string }
  | { readonly kind: 'FOOTWEAR'; readonly system: 'UK'; readonly value: number; readonly label: string }
  | { readonly kind: 'ACCESSORY'; readonly code: 'ONE_SIZE'; readonly label: string };
export interface CatalogueProduct extends ProductSummary {
  readonly productId: string;
  readonly family: 'Apparel' | 'Footwear' | 'Accessories';
  readonly attributeStandardVersion: '1.0.0';
  readonly variantDefiningAttributes: readonly ('Size' | 'Colour')[];
  readonly variants: readonly {
    readonly variantId: string;
    readonly attributes: { readonly size?: CatalogueSize; readonly colour?: { readonly code: 'BLACK'; readonly label: 'Black' } };
  }[];
  readonly media: readonly ProductSummaryMedia[];
}
/** Supported representation only. This brand grants no publication/rights authority. */
export interface ValidatedCataloguePage {
  readonly [validated]: true;
  readonly items: readonly CatalogueProduct[];
  readonly pageSize: number;
  readonly nextCursor: string | null;
}

function object(value: unknown): value is RecordValue {
  return value !== null && typeof value === 'object' && !Array.isArray(value)
    && (Object.getPrototypeOf(value) === Object.prototype || Object.getPrototypeOf(value) === null);
}
function shape(value: unknown, required: string[], optional: string[] = []): value is RecordValue {
  return object(value) && required.every(key => Object.hasOwn(value, key))
    && Reflect.ownKeys(value).every(key => typeof key === 'string' && [...required, ...optional].includes(key));
}
const text = (value: unknown): value is string => typeof value === 'string' && value.trim().length > 0;
const id = (value: unknown): value is string => typeof value === 'string' && /^[A-Za-z0-9_-]{1,128}$/.test(value) && !/\s/.test(value);
function mediaUrl(value: unknown): boolean {
  if (typeof value !== 'string' || !value.startsWith('https://') || /[\s\\\u0000-\u0020\u007f]/.test(value)) return false;
  try { const url = new URL(value); return !!url.hostname && !url.username && !url.password; } catch { return false; }
}
function size(value: unknown, family: unknown): boolean {
  if (!object(value) || !text(value['label'])) return false;
  switch (family) {
    case 'Apparel': return shape(value, ['kind', 'code', 'label']) && value['kind'] === 'APPAREL'
      && ['XS', 'S', 'M', 'L', 'XL', 'XXL', 'XXXL'].includes(value['code'] as string);
    case 'Footwear': return shape(value, ['kind', 'system', 'value', 'label']) && value['kind'] === 'FOOTWEAR'
      && value['system'] === 'UK' && typeof value['value'] === 'number' && value['value'] >= 3 && value['value'] <= 13
      && Number.isInteger(value['value'] * 2);
    case 'Accessories': return shape(value, ['kind', 'code', 'label']) && value['kind'] === 'ACCESSORY'
      && value['code'] === 'ONE_SIZE';
    default: return false;
  }
}
function product(value: unknown): boolean {
  if (!shape(value, ['productId', 'name', 'description', 'family', 'attributeStandardVersion', 'variantDefiningAttributes', 'variants', 'media'])
    || !id(value['productId']) || !text(value['name']) || !text(value['description'])
    || !['Apparel', 'Footwear', 'Accessories'].includes(value['family'] as string) || value['attributeStandardVersion'] !== '1.0.0') return false;
  const defining = value['variantDefiningAttributes'];
  if (!Array.isArray(defining) || defining.length > 2 || new Set(defining).size !== defining.length
    || !defining.every(item => item === 'Size' || item === 'Colour') || !Array.isArray(value['variants']) || !Array.isArray(value['media'])) return false;
  const variantIds = new Set<string>();
  const configurations = new Set<string>();
  for (const variant of value['variants']) {
    if (!shape(variant, ['variantId', 'attributes']) || !id(variant['variantId']) || variantIds.has(variant['variantId'])
      || !shape(variant['attributes'], defining.map(item => item.toLowerCase()))) return false;
    const attributes = variant['attributes'];
    if (defining.includes('Size') && !size(attributes['size'], value['family'])) return false;
    const colour = attributes['colour'];
    if (defining.includes('Colour') && (!shape(colour, ['code', 'label']) || colour['code'] !== 'BLACK' || colour['label'] !== 'Black')) return false;
    const canonicalSize = attributes['size'] as RecordValue | undefined;
    const key = JSON.stringify([canonicalSize?.['kind'], canonicalSize?.['code'] ?? canonicalSize?.['value'], defining.includes('Colour') ? 'BLACK' : null]);
    if (configurations.has(key)) return false;
    configurations.add(key);
    variantIds.add(variant['variantId']);
  }
  const mediaIds = new Set<string>();
  for (const media of value['media']) {
    if (!shape(media, ['mediaId', 'url', 'alternativeText'], ['variantId']) || !id(media['mediaId'])
      || mediaIds.has(media['mediaId']) || !mediaUrl(media['url']) || !text(media['alternativeText'])
      || (Object.hasOwn(media, 'variantId') && (!id(media['variantId']) || !variantIds.has(media['variantId'])))) return false;
    mediaIds.add(media['mediaId']);
  }
  return true;
}
function freeze<T>(value: T): T {
  if (value !== null && typeof value === 'object') {
    Object.values(value).forEach(freeze);
    Object.freeze(value);
  }
  return value;
}

/** Pure JSON-value boundary. No HTTP, eligibility inference, cursor decoding or partial salvage. */
export function validateCataloguePage(input: unknown): ValidatedCataloguePage | null {
  if (!shape(input, ['items', 'pageSize', 'nextCursor']) || !Number.isInteger(input['pageSize'])
    || typeof input['pageSize'] !== 'number' || input['pageSize'] < 1 || input['pageSize'] > 100
    || !Array.isArray(input['items']) || input['items'].length > input['pageSize']
    || !(input['nextCursor'] === null || (typeof input['nextCursor'] === 'string'
      && /^[A-Za-z0-9_-]{1,2048}$/.test(input['nextCursor']) && !/\s/.test(input['nextCursor'])))
    || (input['items'].length === 0 && input['nextCursor'] !== null)) return null;
  let previous: string | undefined;
  for (const item of input['items']) {
    if (!product(item)) return null;
    const current = (item as RecordValue)['productId'] as string;
    // Public IDs are ASCII: lexical comparison matches unsigned UTF-8 byte order.
    if (previous !== undefined && current <= previous) return null;
    previous = current;
  }
  // Detach and freeze the validated JSON snapshot: caller mutation cannot invalidate its brand.
  const copy = structuredClone(input);
  return freeze({ ...copy, [validated]: true }) as unknown as ValidatedCataloguePage;
}
