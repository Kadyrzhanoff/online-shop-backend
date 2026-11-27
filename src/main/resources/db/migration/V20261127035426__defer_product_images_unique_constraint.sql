ALTER TABLE public.product_images
DROP CONSTRAINT uq_product_images_variant_sort;

ALTER TABLE public.product_images
    ADD CONSTRAINT uq_product_images_variant_sort
        UNIQUE (variant_id, sort_order)
    DEFERRABLE INITIALLY DEFERRED;