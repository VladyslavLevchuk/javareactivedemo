CREATE TABLE IF NOT EXISTS tutorial
(
    id          SERIAL NOT NULL,
    title       VARCHAR(255)  NOT NULL,
    description VARCHAR(255)  NOT NULL,
    published   BOOLEAN  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (title, description, published)
    );

INSERT INTO tutorial(title, description, published) values ('title1', 'description1', true) ON CONFLICT (title, description, published) DO NOTHING;
INSERT INTO tutorial(title, description, published) values ('title2', 'description2', false) ON CONFLICT (title, description, published) DO NOTHING;
INSERT INTO tutorial(title, description, published) values ('title3', 'description3', true) ON CONFLICT (title, description, published) DO NOTHING;
INSERT INTO tutorial(title, description, published) values ('title4', 'description4', false) ON CONFLICT (title, description, published) DO NOTHING;
INSERT INTO tutorial(title, description, published) values ('title5', 'description5', true) ON CONFLICT (title, description, published) DO NOTHING;