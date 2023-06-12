create table automaton (
    id               bigserial primary key,
    name             varchar(255)                                           not null,
    type             varchar(255)                                           not null,
    specification_id bigint references specification (id) on delete cascade not null
);

create table automaton_state (
    id           bigserial primary key,
    name         varchar(255)                                       not null,
    type         varchar(255)                                       not null,
    automaton_id bigint references automaton (id) on delete cascade not null
);

create table automaton_shift (
    id             bigserial primary key,
    start_state_id bigint references automaton_state (id) on delete cascade not null,
    end_state_id   bigint references automaton_state (id)                   not null,
    constraint pk_automaton_shift primary key (id)
);

create table automaton_function (
    id           bigserial primary key,
    name         varchar(255)                                       not null,
    automaton_id bigint references automaton (id) on delete cascade not null
);

create table automaton_shift_function (
    shift_id    bigint references automaton_shift (id)    not null,
    function_id bigint references automaton_function (id) not null
);

create table automaton_call (
    function_id   bigint references automaton_function (id) on delete cascade not null,
    automaton_id  bigint references automaton (id) on delete cascade          not null,
    init_state_id bigint references automaton_state (id) on delete cascade    not null,
    primary key (function_id, automaton_id, init_state_id)
);
