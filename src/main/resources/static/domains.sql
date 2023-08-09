
DO $$ 
BEGIN 
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.domains 
        WHERE domain_name = 'biguint'
    ) THEN
        CREATE DOMAIN biguint AS bigint
            CHECK (VALUE >= 0);
    END IF;
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.domains 
        WHERE domain_name = 'smalluint'
    ) THEN
        CREATE DOMAIN smalluint AS smallint
            CHECK (VALUE >= 0);
    END IF;
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.domains 
        WHERE domain_name = 'unum'
    ) THEN
        CREATE DOMAIN unum AS numeric
            CHECK (VALUE >= 0);
    END IF;
END $$;
