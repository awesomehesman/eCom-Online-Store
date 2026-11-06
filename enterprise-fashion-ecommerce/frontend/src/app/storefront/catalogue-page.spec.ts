import { validateCataloguePage } from './catalogue-page';

function fixture() {
  return { items: [{ productId: 'product-a', name: 'Synthetic shirt', description: 'Synthetic description', family: 'Apparel',
    attributeStandardVersion: '1.0.0', variantDefiningAttributes: ['Size', 'Colour'],
    variants: [{ variantId: 'variant-a', attributes: { size: { kind: 'APPAREL', code: 'M', label: 'M' }, colour: { code: 'BLACK', label: 'Black' } } }],
    media: [{ mediaId: 'media-a', url: 'https://example.invalid/a.svg', alternativeText: 'Synthetic shirt', variantId: 'variant-a' }] }],
    pageSize: 20, nextCursor: null as string | null };
}
function change(input: unknown, path: string, value: unknown, remove = false): void {
  const keys = path.split('.');
  let target = input as Record<string, unknown>;
  for (const key of keys.slice(0, -1)) target = target[key] as Record<string, unknown>;
  if (remove) delete target[keys.at(-1)!]; else target[keys.at(-1)!] = value;
}

describe('Pure catalogue page validation', () => {
  it('validates, detaches and deeply freezes the complete supported representation', () => {
    const source = fixture();
    const result = validateCataloguePage(source)!;
    expect(result.items[0].variants[0].attributes.size?.kind).toBe('APPAREL');
    source.items[0].name = 'Changed';
    expect(result.items[0].name).toBe('Synthetic shirt');
    expect(Object.isFrozen(result.items[0].media[0])).toBeTrue();
    expect(Object.isFrozen(result.items)).toBeTrue();
  });
  it('preserves multiple Products, media order and opaque continuation', () => {
    const page = fixture();
    page.items.push({ ...structuredClone(page.items[0]), productId: 'product-b' });
    const media = page.items[0].media[0];
    page.items[0].media = [{ ...media, mediaId: 'z' }, { ...media, mediaId: 'a' }];
    page.nextCursor = 'Opaque_123-next';
    const result = validateCataloguePage(page)!;
    expect(result.items.map(item => item.productId)).toEqual(['product-a', 'product-b']);
    expect(result.items[0].media.map(item => item.mediaId)).toEqual(['z', 'a']);
    expect(result.nextCursor).toBe(page.nextCursor);
  });
  it('accepts Product-level media and family-compatible Attribute configurations', () => {
    const page = fixture();
    change(page, 'items.0.media.0.variantId', undefined, true);
    expect(validateCataloguePage(page)).not.toBeNull();
    change(page, 'items.0.family', 'Footwear');
    change(page, 'items.0.variants.0.attributes.size', { kind: 'FOOTWEAR', system: 'UK', value: 7.5, label: 'UK 7.5' });
    expect(validateCataloguePage(page)).not.toBeNull();
    change(page, 'items.0.family', 'Accessories');
    change(page, 'items.0.variants.0.attributes.size', { kind: 'ACCESSORY', code: 'ONE_SIZE', label: 'One Size' });
    expect(validateCataloguePage(page)).not.toBeNull();
    change(page, 'items.0.variantDefiningAttributes', []);
    change(page, 'items.0.variants.0.attributes', {});
    expect(validateCataloguePage(page)).not.toBeNull();
  });
  it('permits empty children without inventing Product completeness policy', () => {
    const page = fixture();
    change(page, 'items.0.variants', []);
    change(page, 'items.0.media', []);
    expect(validateCataloguePage(page)).not.toBeNull();
  });
  for (const pageSize of [1, 100]) {
    it(`supports page bound ${pageSize}`, () => {
      const page = fixture(); page.pageSize = pageSize;
      if (pageSize === 100) page.items = Array.from({ length: 100 }, (_, i) => ({ ...structuredClone(page.items[0]), productId: `product-${String(i).padStart(3, '0')}` }));
      expect(validateCataloguePage(page)?.items.length).toBe(pageSize);
    });
  }
  it('accepts an empty terminal page', () => {
    expect(validateCataloguePage({ items: [], pageSize: 20, nextCursor: null })?.items).toEqual([]);
  });
  for (const path of ['items', 'pageSize', 'nextCursor', 'items.0.productId', 'items.0.name', 'items.0.description', 'items.0.family',
    'items.0.attributeStandardVersion', 'items.0.variantDefiningAttributes', 'items.0.variants', 'items.0.media',
    'items.0.variants.0.variantId', 'items.0.variants.0.attributes', 'items.0.variants.0.attributes.size.kind',
    'items.0.variants.0.attributes.size.code', 'items.0.variants.0.attributes.size.label',
    'items.0.variants.0.attributes.colour.code', 'items.0.variants.0.attributes.colour.label',
    'items.0.media.0.mediaId', 'items.0.media.0.url', 'items.0.media.0.alternativeText']) {
    it(`rejects missing ${path}`, () => { const page = fixture(); change(page, path, undefined, true); expect(validateCataloguePage(page)).toBeNull(); });
  }
  const invalid: [string, unknown][] = [
    ['extra', true], ['items', null], ['pageSize', '20'], ['pageSize', 0], ['pageSize', 101], ['pageSize', 1.5],
    ['nextCursor', ''], ['nextCursor', 'a\n'], ['nextCursor', 'a'.repeat(2049)], ['nextCursor', 1],
    ['items.0.extra', true], ['items.0.productId', 'a\n'], ['items.0.productId', 'a'.repeat(129)], ['items.0.productId', 1],
    ['items.0.name', null], ['items.0.name', ' '], ['items.0.description', 1], ['items.0.family', 'Unknown'],
    ['items.0.attributeStandardVersion', '2.0.0'], ['items.0.variantDefiningAttributes', ['Size', 'Size']],
    ['items.0.variantDefiningAttributes', ['Fit']], ['items.0.variantDefiningAttributes', null],
    ['items.0.variants', null], ['items.0.media', null], ['items.0.variants.0.variantId', 'bad id'],
    ['items.0.variants.0.extra', true], ['items.0.variants.0.attributes', {}],
    ['items.0.variants.0.attributes.extra', true], ['items.0.variants.0.attributes.size.extra', true],
    ['items.0.variants.0.attributes.size.kind', 'FOOTWEAR'], ['items.0.variants.0.attributes.size.code', 'UNKNOWN'],
    ['items.0.variants.0.attributes.size.label', 12], ['items.0.variants.0.attributes.colour.extra', true],
    ['items.0.variants.0.attributes.colour.code', 'RED'], ['items.0.variants.0.attributes.colour.label', 'Red'],
    ['items.0.media.0.extra', true], ['items.0.media.0.mediaId', 'bad id'], ['items.0.media.0.variantId', 'foreign'],
    ['items.0.media.0.variantId', undefined], ['items.0.media.0.alternativeText', ' '],
    ...['http://example.invalid/a', 'https://', 'javascript:alert(1)', '/relative', 'https://user:secret@example.invalid/a',
      'https://example.invalid/a b', 'https://example.invalid/a\n', 'https://example.invalid/\\a'].map(url => ['items.0.media.0.url', url] as [string, unknown]),
  ];
  invalid.forEach(([path, value], index) => {
    it(`rejects invalid field case ${index}: ${path}`, () => { const page = fixture(); change(page, path, value); expect(validateCataloguePage(page)).toBeNull(); });
  });
  it('rejects duplicate Product IDs and out-of-order pages rather than sorting', () => {
    const page = fixture(); page.items.push(structuredClone(page.items[0]));
    expect(validateCataloguePage(page)).toBeNull();
    page.items[0].productId = 'product-z';
    expect(validateCataloguePage(page)).toBeNull();
  });
  it('rejects duplicate Variant IDs and canonical configurations independently of labels', () => {
    const page = fixture(); page.items[0].variants.push(structuredClone(page.items[0].variants[0]));
    expect(validateCataloguePage(page)).toBeNull();
    page.items[0].variants[1].variantId = 'variant-b';
    expect(validateCataloguePage(page)).toBeNull();
    page.items[0].variants[1].attributes.size.code = 'L'; page.items[0].variants[1].attributes.size.label = 'L';
    expect(validateCataloguePage(page)).not.toBeNull();
  });
  it('rejects duplicate media and withholds the whole page when another Product is malformed', () => {
    const page = fixture(); page.items[0].media.push(structuredClone(page.items[0].media[0]));
    expect(validateCataloguePage(page)).toBeNull();
    page.items[0].media.pop(); page.items.push({ ...structuredClone(page.items[0]), productId: 'product-b', name: '' });
    expect(validateCataloguePage(page)).toBeNull();
  });
  it('rejects invalid footwear ranges, increments, systems and extensions', () => {
    const page = fixture(); change(page, 'items.0.family', 'Footwear');
    for (const value of [2.5, 13.5, 7.25, NaN, Infinity]) {
      change(page, 'items.0.variants.0.attributes.size', { kind: 'FOOTWEAR', system: 'UK', value, label: 'UK size' });
      expect(validateCataloguePage(page)).toBeNull();
    }
    change(page, 'items.0.family', 'Accessories');
    change(page, 'items.0.variants.0.attributes.size', { kind: 'ACCESSORY', code: 'NEW', label: 'New' });
    expect(validateCataloguePage(page)).toBeNull();
  });
  it('rejects malformed envelopes, excessive items and empty continuation', () => {
    for (const value of [null, [], 'page', {}, { items: [], pageSize: 20, nextCursor: 'next' }]) expect(validateCataloguePage(value)).toBeNull();
    const page = fixture(); page.pageSize = 1; page.items.push({ ...structuredClone(page.items[0]), productId: 'product-b' });
    expect(validateCataloguePage(page)).toBeNull();
  });
  it('has no network side effects', () => {
    const fetch = spyOn(window, 'fetch'); const xhr = spyOn(XMLHttpRequest.prototype, 'open');
    validateCataloguePage(fixture()); expect(fetch).not.toHaveBeenCalled(); expect(xhr).not.toHaveBeenCalled();
  });
});
