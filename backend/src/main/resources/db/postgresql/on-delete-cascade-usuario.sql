-- Ejecutar una vez en PostgreSQL si la base ya existe y sus FKs fueron creadas sin ON DELETE CASCADE.
-- Hibernate ddl-auto=update no cambia automaticamente la accion ON DELETE de constraints existentes.

DO $$
DECLARE
    constraint_name text;
BEGIN
    FOR constraint_name IN
        SELECT con.conname
        FROM pg_constraint con
        JOIN pg_class rel ON rel.oid = con.conrelid
        JOIN pg_namespace nsp ON nsp.oid = rel.relnamespace
        JOIN pg_attribute att ON att.attrelid = rel.oid AND att.attnum = ANY (con.conkey)
        WHERE con.contype = 'f'
          AND nsp.nspname = 'public'
          AND rel.relname = 'reserva'
          AND att.attname = 'id_usuario'
    LOOP
        EXECUTE format('ALTER TABLE public.reserva DROP CONSTRAINT %I', constraint_name);
    END LOOP;

    ALTER TABLE public.reserva
        ADD CONSTRAINT fk_reserva_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES public.usuario(id)
        ON DELETE CASCADE;

    FOR constraint_name IN
        SELECT con.conname
        FROM pg_constraint con
        JOIN pg_class rel ON rel.oid = con.conrelid
        JOIN pg_namespace nsp ON nsp.oid = rel.relnamespace
        JOIN pg_attribute att ON att.attrelid = rel.oid AND att.attnum = ANY (con.conkey)
        WHERE con.contype = 'f'
          AND nsp.nspname = 'public'
          AND rel.relname = 'rol_usuario'
          AND att.attname = 'id_usuario'
    LOOP
        EXECUTE format('ALTER TABLE public.rol_usuario DROP CONSTRAINT %I', constraint_name);
    END LOOP;

    ALTER TABLE public.rol_usuario
        ADD CONSTRAINT fk_rol_usuario_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES public.usuario(id)
        ON DELETE CASCADE;
END $$;
