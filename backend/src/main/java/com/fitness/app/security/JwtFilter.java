package com.fitness.app.security;

import com.fitness.app.rol.entity.Rol;
import com.fitness.app.rol.repository.RolRepository;
import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;
    private final RolUsuarioRepository rolUsuarioRepository;
    private final RolRepository rolRepository;

    public JwtFilter(
            UsuarioRepository usuarioRepository,
            RolUsuarioRepository rolUsuarioRepository,
            RolRepository rolRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.rolRepository = rolRepository;
    }

    private final String SECRET_KEY =
            "mi_clave_super_secreta_muy_larga_123456789";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {

                Key key = Keys.hmacShaKeyFor(
                        SECRET_KEY.getBytes(StandardCharsets.UTF_8)
                );

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();

                Usuario usuario = usuarioRepository
                        .findByEmail(email)
                        .orElseThrow();

                List<RolUsuario> rolesUsuario =
                        rolUsuarioRepository.findByUsuarioId(usuario.getId());

                List<SimpleGrantedAuthority> authorities =
                        new ArrayList<>();

                for (RolUsuario ru : rolesUsuario) {

                    Rol rol = rolRepository
                            .findById(ru.getIdRol())
                            .orElseThrow();

                    authorities.add(
                            new SimpleGrantedAuthority(
                                    rol.getNombre()
                            )
                    );
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

            } catch (Exception e) {

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
