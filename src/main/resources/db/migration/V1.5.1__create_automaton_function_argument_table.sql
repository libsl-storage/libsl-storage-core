create table automaton_function_argument (
    id          bigserial primary key,
    name        varchar(255)                                                not null,
    type        varchar(255)                                                not null,
    function_id bigint references automaton_function (id) on delete cascade not null
);
