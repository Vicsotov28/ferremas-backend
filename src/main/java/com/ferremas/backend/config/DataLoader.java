package com.ferremas.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ferremas.backend.model.Producto;
import com.ferremas.backend.model.Usuario;
import com.ferremas.backend.repository.ProductoRepository;
import com.ferremas.backend.repository.UsuarioRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder) {

        return args -> {

            if (productoRepository.count() == 0) {

                Producto p1 = new Producto();
                p1.setCodigoProducto("FER-12345");
                p1.setMarca("Bosch");
                p1.setCodigoMarca("BOS-67890");
                p1.setNombre("Taladro Percutor Bosch");
                p1.setModelo("GSB 13 RE");
                p1.setCategoria("Herramientas Eléctricas");
                p1.setDescripcion("Taladro percutor profesional para construcción");
                p1.setPrecio(89090.0);
                p1.setStock(12);
                productoRepository.save(p1);

                Producto p2 = new Producto();
                p2.setCodigoProducto("FER-23456");
                p2.setMarca("Stanley");
                p2.setCodigoMarca("STA-11223");
                p2.setNombre("Martillo Stanley");
                p2.setModelo("STHT51391");
                p2.setCategoria("Herramientas Manuales");
                p2.setDescripcion("Martillo de acero con mango ergonómico");
                p2.setPrecio(8990.0);
                p2.setStock(25);
                productoRepository.save(p2);

                Producto p3 = new Producto();
                p3.setCodigoProducto("FER-34567");
                p3.setMarca("Sipa");
                p3.setCodigoMarca("SIP-44556");
                p3.setNombre("Pintura Blanca Sipa");
                p3.setModelo("Interior Blanco 1GL");
                p3.setCategoria("Pinturas");
                p3.setDescripcion("Galón de pintura blanca para interiores");
                p3.setPrecio(15990.0);
                p3.setStock(15);
                productoRepository.save(p3);

                Producto p4 = new Producto();
                p4.setCodigoProducto("FER-45678");
                p4.setMarca("Makita");
                p4.setCodigoMarca("MAK-77889");
                p4.setNombre("Sierra Circular Makita");
                p4.setModelo("HS7600");
                p4.setCategoria("Herramientas Eléctricas");
                p4.setDescripcion("Sierra circular eléctrica de alto rendimiento");
                p4.setPrecio(89990.0);
                p4.setStock(8);
                productoRepository.save(p4);
            }

            if (usuarioRepository.count() == 0) {

                usuarioRepository.save(new Usuario(
                        null,
                        "Cliente Demo",
                        "cliente@ferremas.cl",
                        passwordEncoder.encode("1234"),
                        "CLIENTE"
                ));

                usuarioRepository.save(new Usuario(
                        null,
                        "Vendedor Demo",
                        "vendedor@ferremas.cl",
                        passwordEncoder.encode("1234"),
                        "VENDEDOR"
                ));

                usuarioRepository.save(new Usuario(
                        null,
                        "Bodeguero Demo",
                        "bodeguero@ferremas.cl",
                        passwordEncoder.encode("1234"),
                        "BODEGUERO"
                ));

                usuarioRepository.save(new Usuario(
                        null,
                        "Contador Demo",
                        "contador@ferremas.cl",
                        passwordEncoder.encode("1234"),
                        "CONTADOR"
                ));

                usuarioRepository.save(new Usuario(
                        null,
                        "Administrador Demo",
                        "admin@ferremas.cl",
                        passwordEncoder.encode("1234"),
                        "ADMINISTRADOR"
                ));
            }
        };
    }
}