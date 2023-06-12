create table specification (
    id           bigserial primary key,
    name         varchar(255)                                     not null,
    description  varchar(255)                                     not null,
    path         varchar(255)                                     not null,
    content      varchar(255)                                     not null,
    directory_id bigint references directory (id) on delete cascade,
    owner_id     bigint references account (id) on delete cascade not null,
    unique (name, directory_id)
);

create table specification_error (
    id               bigserial primary key,
    message          varchar(255)                                           not null,
    start_position   integer                                                not null,
    end_position     integer                                                not null,
    specification_id bigint references specification (id) on delete cascade not null
);
