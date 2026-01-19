ALTER TABLE order_item
DROP
COLUMN price;

ALTER TABLE order_item
    ADD price DOUBLE PRECISION;

ALTER TABLE spare_parts
DROP
COLUMN price;

ALTER TABLE spare_parts
    ADD price DOUBLE PRECISION;