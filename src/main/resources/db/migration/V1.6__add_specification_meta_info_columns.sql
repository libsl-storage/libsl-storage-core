alter table specification
    add column libsl_version varchar(255);

alter table specification
    add column library_name varchar(255);

alter table specification
    add column library_version varchar(255);

alter table specification
    add column library_language varchar(255);

alter table specification
    add column library_url varchar(255);

update specification
set library_name = library_tag.name
from (select specification_id, name
      from tag
      where group_id in (select id from tag_group where name = 'LIBRARY')) as library_tag
where specification.id = library_tag.specification_id;

update specification
set library_version = library_tag.name
from (select specification_id, name
      from tag
      where group_id in (select id from tag_group where name = 'VERSION')) as library_tag
where specification.id = library_tag.specification_id;

update specification
set library_language = library_tag.name
from (select specification_id, name
      from tag
      where group_id in (select id from tag_group where name = 'LANGUAGE')) as library_tag
where specification.id = library_tag.specification_id;

update specification
set library_url = library_tag.name
from (select specification_id, name
      from tag
      where group_id in (select id from tag_group where name = 'URL')) as library_tag
where specification.id = library_tag.specification_id;

delete
from tag
where group_id in (select id
                   from tag_group
                   where tag_group.name in ('VERSION', 'LANGUAGE', 'URL', 'OTHER'));

alter table tag
    drop constraint tag_group_id_fkey;
alter table tag
    drop column group_id;
drop table tag_group;
