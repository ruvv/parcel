create user userservice with
  password 'SCRAM-SHA-256$4096:k9A8rIJpFyZ98cO0bvfTSA==$Zp9waWEhd6QeoMOkd1iwxNEB3oyhKOqF05KCgz+pPGA=:9oMuDJx19hA/kV2ms65s3BBrn/vmGl/haHr/gARyLtg=';

create database userdb;

grant all privileges on database userdb to userservice;

\connect userdb

grant all on schema public to userservice;
