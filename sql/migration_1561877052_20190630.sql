SET CLIENT_MIN_MESSAGES = WARNING;BEGIN;    drop view if exists teacher_info;    create view teacher_info as (select * from "user" where id in (select user_id from user_roles where role_id in (select id from "role" where code = 'TEACHER')));    drop schema if exists teacher;    create schema teacher;    set search_path = teacher, public;COMMIT;