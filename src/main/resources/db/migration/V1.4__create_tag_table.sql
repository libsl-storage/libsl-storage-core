create table tag_group (
    id   bigserial primary key,
    name varchar(255) unique not null
);

create table tag (
    id               bigserial primary key,
    name             varchar(255)                                           not null,
    group_id         bigint references tag_group (id) on delete cascade     not null,
    specification_id bigint references specification (id) on delete cascade not null
);
