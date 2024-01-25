CREATE OR REPLACE FUNCTION log_tenants() RETURNS trigger AS
$$
BEGIN
    IF TG_OP = 'UPDATE' AND NEW.house_id <> OLD.house_id OR TG_OP = 'INSERT' THEN
        INSERT INTO house_history (house_id, person_id, date, type) VALUES (NEW.house_id, NEW.id, now(), 'TENANT');
    END IF;
    IF TG_OP = 'UPDATE' AND NEW.house_id <> OLD.house_id OR TG_OP = 'DELETE'THEN
        INSERT INTO house_history (house_id, person_id, date, type) VALUES (NEW.house_id, NEW.id, now(), 'TENANT');
        INSERT INTO house_history (house_id, person_id, date, type) VALUES (OLD.house_id, OLD.id, now(), 'TENANT');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


