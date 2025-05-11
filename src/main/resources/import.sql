CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
INSERT INTO siscon_genre (siscon_uuid, siscon_code, siscon_description, siscon_created_at, siscon_active) values (uuid_generate_v4(), 'F', 'Female', now(), true);
INSERT INTO siscon_genre (siscon_uuid, siscon_code, siscon_description, siscon_created_at, siscon_active) values (uuid_generate_v4(), 'M', 'Male', now(), true);
INSERT INTO siscon_position (siscon_uuid, siscon_code, siscon_description, siscon_created_at, siscon_active) values (uuid_generate_v4(), 'A1', 'Analist Nivel 1', now(), true);
INSERT INTO siscon_position (siscon_uuid, siscon_code, siscon_description, siscon_created_at, siscon_active) values (uuid_generate_v4(), 'S1', 'Supervisor Nivel 1', now(), true);
INSERT INTO siscon_position (siscon_uuid, siscon_code, siscon_description, siscon_created_at, siscon_active) values (uuid_generate_v4(), 'M1', 'Manager Nivel 1', now(), true);