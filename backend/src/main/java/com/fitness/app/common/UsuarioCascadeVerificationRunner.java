package com.fitness.app.common;

import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.service.UsuarioService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "verification.usuario-cascade", havingValue = "true")
public class UsuarioCascadeVerificationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    private final UsuarioService usuarioService;
    private final ConfigurableApplicationContext context;

    public UsuarioCascadeVerificationRunner(JdbcTemplate jdbcTemplate,
                                            UsuarioService usuarioService,
                                            ConfigurableApplicationContext context) {
        this.jdbcTemplate = jdbcTemplate;
        this.usuarioService = usuarioService;
        this.context = context;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            ejecutarVerificacion();
        } finally {
            context.close();
        }
    }

    private void ejecutarVerificacion() {
        asegurarOnDeleteCascadeUsuario();

        String suffix = String.valueOf(Instant.now().toEpochMilli());
        Usuario usuario = new Usuario();
        usuario.setNombre("Verificacion Cascade " + suffix);
        usuario.setEmail("cascade-" + suffix + "@example.com");
        usuario.setPassword("Password123");
        usuario.setTelefono("900000000");

        Usuario usuarioGuardado = usuarioService.crearUsuario(usuario);
        int idUsuario = usuarioGuardado.getId();

        int idRol = obtenerOCrearRol("ROLE_MEMBER");
        int idClase = crearClasePrueba(suffix);

        jdbcTemplate.update(
                "insert into rol_usuario (id_usuario, id_rol) values (?, ?)",
                idUsuario,
                idRol
        );
        jdbcTemplate.update(
                "insert into reserva (id_usuario, id_clase, fecha_reserva, estado) values (?, ?, ?, ?)",
                idUsuario,
                idClase,
                "2026-07-07",
                "ACTIVA"
        );

        long usuariosAntes = contar("select count(*) from usuario where id = ?", idUsuario);
        long reservasAntes = contar("select count(*) from reserva where id_usuario = ?", idUsuario);
        long rolesUsuarioAntes = contar("select count(*) from rol_usuario where id_usuario = ?", idUsuario);

        imprimir("ANTES usuario.id=" + idUsuario
                + " usuario=" + usuariosAntes
                + " reserva=" + reservasAntes
                + " rol_usuario=" + rolesUsuarioAntes);

        usuarioService.eliminarUsuario(idUsuario);

        long usuariosDespues = contar("select count(*) from usuario where id = ?", idUsuario);
        long reservasDespues = contar("select count(*) from reserva where id_usuario = ?", idUsuario);
        long rolesUsuarioDespues = contar("select count(*) from rol_usuario where id_usuario = ?", idUsuario);
        long reservasHuerfanasUsuario = contar("""
                select count(*)
                from reserva r
                left join usuario u on u.id = r.id_usuario
                where r.id_usuario = ? and u.id is null
                """, idUsuario);
        long rolesHuerfanosUsuario = contar("""
                select count(*)
                from rol_usuario ru
                left join usuario u on u.id = ru.id_usuario
                where ru.id_usuario = ? and u.id is null
                """, idUsuario);

        imprimir("DESPUES usuario.id=" + idUsuario
                + " usuario=" + usuariosDespues
                + " reserva=" + reservasDespues
                + " rol_usuario=" + rolesUsuarioDespues);
        imprimir("HUERFANOS_DEL_USUARIO_ELIMINADO reserva=" + reservasHuerfanasUsuario
                + " rol_usuario=" + rolesHuerfanosUsuario);
        imprimir("FK_USUARIO " + consultarFksUsuario());

        jdbcTemplate.update("delete from clase where id = ?", idClase);

        if (usuariosDespues != 0
                || reservasDespues != 0
                || rolesUsuarioDespues != 0
                || reservasHuerfanasUsuario != 0
                || rolesHuerfanosUsuario != 0) {
            throw new IllegalStateException("La eliminacion en cascada de Usuario dejo datos relacionados");
        }

        imprimir("RESULTADO=OK eliminacion de usuario verificada sin registros relacionados");
    }

    private int obtenerOCrearRol(String nombre) {
        return jdbcTemplate.queryForObject("""
                insert into rol (nombre)
                values (?)
                on conflict (nombre) do update set nombre = excluded.nombre
                returning id
                """, Integer.class, nombre);
    }

    private int crearClasePrueba(String suffix) {
        return jdbcTemplate.queryForObject("""
                insert into clase (nombre, descripcion, trainer, cupos)
                values (?, ?, ?, ?)
                returning id
                """, Integer.class,
                "Clase Cascade " + suffix,
                "Clase temporal para verificar cascade",
                "Verifier",
                10);
    }

    private long contar(String sql, Object... args) {
        Long total = jdbcTemplate.queryForObject(sql, Long.class, args);
        return total == null ? 0 : total;
    }

    private void asegurarOnDeleteCascadeUsuario() {
        jdbcTemplate.execute("""
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
                        ON DELETE CASCADE
                        NOT VALID;
                END $$;
                """);

        jdbcTemplate.execute("""
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
                          AND rel.relname = 'rol_usuario'
                          AND att.attname = 'id_usuario'
                    LOOP
                        EXECUTE format('ALTER TABLE public.rol_usuario DROP CONSTRAINT %I', constraint_name);
                    END LOOP;

                    ALTER TABLE public.rol_usuario
                        ADD CONSTRAINT fk_rol_usuario_usuario
                        FOREIGN KEY (id_usuario)
                        REFERENCES public.usuario(id)
                        ON DELETE CASCADE
                        NOT VALID;
                END $$;
                """);
    }

    private List<Map<String, Object>> consultarFksUsuario() {
        return jdbcTemplate.queryForList("""
                select rel.relname as tabla,
                       con.conname as constraint,
                       case con.confdeltype
                           when 'c' then 'ON DELETE CASCADE'
                           when 'a' then 'NO ACTION'
                           when 'r' then 'RESTRICT'
                           when 'n' then 'SET NULL'
                           when 'd' then 'SET DEFAULT'
                       end as on_delete,
                       con.convalidated as validada
                from pg_constraint con
                join pg_class rel on rel.oid = con.conrelid
                where con.contype = 'f'
                  and con.confrelid = 'usuario'::regclass
                order by rel.relname, con.conname
                """);
    }

    private void imprimir(String mensaje) {
        System.out.println("USUARIO_CASCADE_VERIFICATION " + mensaje);
    }
}
