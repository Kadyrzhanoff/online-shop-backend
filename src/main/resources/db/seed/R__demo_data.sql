-- LOCAL DEVELOPMENT DEMO DATA ONLY. Never enable db/seed on production.
-- Repeatable Flyway migration: safe to rerun without duplicating the named demo records.
-- Product uniqueness here is logical (category + name), not enforced by a DB constraint.

INSERT INTO public.categories (name) VALUES ('Men'), ('Women'), ('Kids')
ON CONFLICT (name) DO NOTHING;

WITH demo(category_name, product_name, price) AS (
    VALUES
        ('Men', 'Classic Cotton T-Shirt', 1490.00),
        ('Men', 'Oxford Shirt', 2890.00),
        ('Men', 'Slim Fit Jeans', 3490.00),
        ('Men', 'Cargo Pants', 3290.00),
        ('Men', 'Zip Hoodie', 3990.00),
        ('Men', 'Crewneck Sweatshirt', 2990.00),
        ('Men', 'Denim Jacket', 5490.00),
        ('Men', 'Puffer Vest', 5990.00),
        ('Men', 'Chino Shorts', 2390.00),
        ('Men', 'Polo Shirt', 2190.00),
        ('Women', 'Relaxed T-Shirt', 1490.00),
        ('Women', 'Linen Blouse', 2790.00),
        ('Women', 'Wide Leg Jeans', 3690.00),
        ('Women', 'Pleated Skirt', 2990.00),
        ('Women', 'Knit Cardigan', 3890.00),
        ('Women', 'Midi Dress', 4990.00),
        ('Women', 'Oversized Hoodie', 4190.00),
        ('Women', 'Tailored Trousers', 3990.00),
        ('Women', 'Denim Jacket', 5290.00),
        ('Women', 'Cotton Top', 1290.00),
        ('Kids', 'Graphic T-Shirt', 990.00),
        ('Kids', 'Cotton Hoodie', 2490.00),
        ('Kids', 'Jogger Pants', 1990.00),
        ('Kids', 'Denim Jeans', 2490.00),
        ('Kids', 'Rain Jacket', 3890.00),
        ('Kids', 'School Polo', 1590.00),
        ('Kids', 'Fleece Sweatshirt', 2290.00),
        ('Kids', 'Summer Shorts', 1390.00),
        ('Kids', 'Knit Sweater', 2790.00),
        ('Kids', 'Track Jacket', 3190.00)
)
INSERT INTO public.products (name, description, price, category_id, active)
SELECT d.product_name, 'Demo product for local development', d.price, c.id, TRUE
FROM demo d
JOIN public.categories c ON c.name = d.category_name
WHERE NOT EXISTS (
    SELECT 1 FROM public.products p
    WHERE p.category_id = c.id AND p.name = d.product_name
);

-- Add one default color variant to each of the named demo products.
WITH demo(category_name, product_name) AS (
    VALUES
        ('Men', 'Classic Cotton T-Shirt'),
        ('Men', 'Oxford Shirt'),
        ('Men', 'Slim Fit Jeans'),
        ('Men', 'Cargo Pants'),
        ('Men', 'Zip Hoodie'),
        ('Men', 'Crewneck Sweatshirt'),
        ('Men', 'Denim Jacket'),
        ('Men', 'Puffer Vest'),
        ('Men', 'Chino Shorts'),
        ('Men', 'Polo Shirt'),
        ('Women', 'Relaxed T-Shirt'),
        ('Women', 'Linen Blouse'),
        ('Women', 'Wide Leg Jeans'),
        ('Women', 'Pleated Skirt'),
        ('Women', 'Knit Cardigan'),
        ('Women', 'Midi Dress'),
        ('Women', 'Oversized Hoodie'),
        ('Women', 'Tailored Trousers'),
        ('Women', 'Denim Jacket'),
        ('Women', 'Cotton Top'),
        ('Kids', 'Graphic T-Shirt'),
        ('Kids', 'Cotton Hoodie'),
        ('Kids', 'Jogger Pants'),
        ('Kids', 'Denim Jeans'),
        ('Kids', 'Rain Jacket'),
        ('Kids', 'School Polo'),
        ('Kids', 'Fleece Sweatshirt'),
        ('Kids', 'Summer Shorts'),
        ('Kids', 'Knit Sweater'),
        ('Kids', 'Track Jacket')
)
INSERT INTO public.product_variants (color, product_id, color_image_url, product_image_url)
SELECT 'Black', p.id, NULL, NULL
FROM demo d
JOIN public.categories c ON c.name = d.category_name
JOIN public.products p ON p.category_id = c.id AND p.name = d.product_name
WHERE NOT EXISTS (
    SELECT 1 FROM public.product_variants pv
    WHERE pv.product_id = p.id AND pv.color = 'Black'
);

-- Sizes and quantities for the Black variant of demo products.
WITH demo(category_name, product_name) AS (
    VALUES
        ('Men', 'Classic Cotton T-Shirt'),
        ('Men', 'Oxford Shirt'),
        ('Men', 'Slim Fit Jeans'),
        ('Men', 'Cargo Pants'),
        ('Men', 'Zip Hoodie'),
        ('Men', 'Crewneck Sweatshirt'),
        ('Men', 'Denim Jacket'),
        ('Men', 'Puffer Vest'),
        ('Men', 'Chino Shorts'),
        ('Men', 'Polo Shirt'),
        ('Women', 'Relaxed T-Shirt'),
        ('Women', 'Linen Blouse'),
        ('Women', 'Wide Leg Jeans'),
        ('Women', 'Pleated Skirt'),
        ('Women', 'Knit Cardigan'),
        ('Women', 'Midi Dress'),
        ('Women', 'Oversized Hoodie'),
        ('Women', 'Tailored Trousers'),
        ('Women', 'Denim Jacket'),
        ('Women', 'Cotton Top'),
        ('Kids', 'Graphic T-Shirt'),
        ('Kids', 'Cotton Hoodie'),
        ('Kids', 'Jogger Pants'),
        ('Kids', 'Denim Jeans'),
        ('Kids', 'Rain Jacket'),
        ('Kids', 'School Polo'),
        ('Kids', 'Fleece Sweatshirt'),
        ('Kids', 'Summer Shorts'),
        ('Kids', 'Knit Sweater'),
        ('Kids', 'Track Jacket')
)
INSERT INTO public.variant_stocks (size, quantity, variant_id)
SELECT sizes.size, 10, pv.id
FROM demo d
JOIN public.categories c ON c.name = d.category_name
JOIN public.products p ON p.category_id = c.id AND p.name = d.product_name
JOIN public.product_variants pv ON pv.product_id = p.id AND pv.color = 'Black'
CROSS JOIN (VALUES ('S'), ('M'), ('L'), ('XL')) AS sizes(size)
ON CONFLICT (variant_id, size) DO NOTHING;
