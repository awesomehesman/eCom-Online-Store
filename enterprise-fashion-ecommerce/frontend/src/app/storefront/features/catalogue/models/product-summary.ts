/** Presentation projection of a contract-validated Catalogue Product. */
export interface ProductSummary {
  readonly name: string;
  readonly description: string;
  readonly productId?: string;
  readonly variants?: readonly { readonly variantId: string }[];
  readonly media?: readonly ProductSummaryMedia[];
  readonly family?: 'Apparel' | 'Footwear' | 'Accessories';
}

/** Media fields mirror the Product Catalogue wire contract. */
export interface ProductSummaryMedia {
  readonly mediaId: string;
  readonly url: string;
  readonly alternativeText: string;
  readonly variantId?: string;
}
