# --- !Ups

create table "shit" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "name" varchar not null,
  "comment" int not null
);

# --- !Downs

drop table "shit" if exists;