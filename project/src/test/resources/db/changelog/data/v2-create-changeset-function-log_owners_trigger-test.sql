CREATE OR REPLACE FUNCTION log_owners_trigger() RETURNS trigger AS
$$
BEGIN
    IF TG_OP = 'INSERT' OR TG_OP = 'UPDATE' THEN
        INSERT INTO house_history (house_id, person_id, date,type) VALUES (NEW.house_id, NEW.person_id, now(),'OWNER');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

