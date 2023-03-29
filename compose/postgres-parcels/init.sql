create user parcelservice with
  password 'SCRAM-SHA-256$4096:zd6X87DSAA94A4MBH/ONHA==$hy60XWtpW598WBKIk+BRBueX81WGyL83+9rnFUjmERk=:8nDim5k8fR1Fwk2Z4AYMvM/OjR//WziaO7dnwQjs+Pk=';

create database parceldb;

grant all privileges on database parceldb to parcelservice;

\connect parceldb

grant all on schema public to parcelservice;
