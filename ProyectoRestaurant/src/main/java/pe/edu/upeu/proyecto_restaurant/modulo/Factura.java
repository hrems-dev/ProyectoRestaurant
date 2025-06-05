package pe.edu.upeu.proyecto_restaurant.modulo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "factura")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long idFactura;

    @Column(name = "codigo", length = 20, unique = true)
    private String codigo;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;

    @OneToOne
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido")
    private Pedido pedido;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "igv")
    private Double igv;

    @Column(name = "total")
    private Double total;

    @Column(name = "estado", length = 20)
    private String estado;
} 