package pe.edu.upeu.proyecto_restaurant.modulo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "detalle_pedido")
public class DetPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Long idDetallePedido;

    @ManyToOne
    @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "igv")
    private Double igv;

    @Column(name = "total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura")
    private Factura factura;

    @Column(name = "estado", length = 20)
    private String estado;
} 