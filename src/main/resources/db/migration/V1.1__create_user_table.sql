create table account (
    id                 bigserial primary key,
    name               varchar not null,
    email              varchar not null,
    password           varchar not null,
    profile_image_path varchar
);

create table role (
    id   bigserial primary key,
    name varchar unique not null
);

create table account_role (
    account_id bigint references account (id) on delete cascade not null,
    role_id    bigint references role (id) on delete cascade    not null,
    primary key (account_id, role_id)
);
