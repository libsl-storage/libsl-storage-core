alter table automaton_call drop constraint automaton_call_pkey;
alter table automaton_call drop constraint automaton_call_automaton_id_fkey;
alter table automaton_call drop column automaton_id;
alter table automaton_call add primary key (function_id, init_state_id);
