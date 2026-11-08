CREATE TABLE recipe (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description VARCHAR(2000),
    uploaded TIMESTAMP,
    url TEXT
);

CREATE TABLE recipe_ingredient (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
    ingredient TEXT NOT NULL
);

CREATE TABLE recipe_step (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL REFERENCES recipe(id) ON DELETE CASCADE,
    step_order INT NOT NULL,
    step TEXT NOT NULL
);