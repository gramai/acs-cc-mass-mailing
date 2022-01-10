create table bank_users
(
    id               VARCHAR(36)  NOT NULL,
    email            VARCHAR(255) NOT NULL UNIQUE,
    iban             VARCHAR(36)  NOT NULL UNIQUE,
    available_amount VARCHAR(36),
    created_on       TIMESTAMP    NOT NULL,
    last_updated_on  TIMESTAMP,
    PRIMARY KEY (id)
);

create sequence seq start 1;

create or replace function insert_users (users_count integer) returns void as $$
DECLARE
    id_client_curent INTEGER := 1;
    seq_start INTEGER := 1;
begin
    id_client_curent := nextval('seq');
    seq_start := id_client_curent;
    loop
        raise notice '% % %', id_client_curent, seq_start, users_count;
        if (id_client_curent - seq_start <= users_count) then
            execute format($fmt$ insert into bank_users(id, email, iban, available_amount, created_on, last_updated_on)
   	values (%1$L::varchar(36), concat('test_user_', %1$L::varchar(36), '@gmail.com'), concat('RO49AAAA1B310000000', %1$L::varchar(36)), %1$L::varchar(36), now(), now())$fmt$, id_client_curent);
            id_client_curent := nextval('seq');
        else
            return;
        end if;
    end loop;
end $$ language 'plpgsql';


select insert_users(10000);