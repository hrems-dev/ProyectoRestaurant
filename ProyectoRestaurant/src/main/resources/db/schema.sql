-- Tabla de Categorías
CREATE TABLE IF NOT EXISTS categoria (
    categoria_id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT
);

-- Tabla de Productos
CREATE TABLE IF NOT EXISTS producto (
    producto_id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    descripcion TEXT,
    categoria_id INTEGER,
    stock INTEGER DEFAULT 0,
    estado VARCHAR(20) DEFAULT 'ACTIVO',
    FOREIGN KEY (categoria_id) REFERENCES categoria(categoria_id)
);

-- Tabla de Mesas
CREATE TABLE IF NOT EXISTS mesa (
    mesa_id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero INTEGER NOT NULL UNIQUE,
    capacidad INTEGER NOT NULL,
    estado VARCHAR(20) DEFAULT 'DISPONIBLE'
);

-- Tabla de Empleados
CREATE TABLE IF NOT EXISTS empleado (
    empleado_id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(8) UNIQUE,
    cargo VARCHAR(50) NOT NULL,
    telefono VARCHAR(15),
    email VARCHAR(100)
);

-- Tabla de Clientes
CREATE TABLE IF NOT EXISTS cliente (
    cliente_id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    dni VARCHAR(8) UNIQUE,
    telefono VARCHAR(15),
    email VARCHAR(100)
);

-- Tabla de Pedidos
CREATE TABLE IF NOT EXISTS pedido (
    pedido_id INTEGER PRIMARY KEY AUTOINCREMENT,
    mesa_id INTEGER,
    cliente_id INTEGER,
    empleado_id INTEGER,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) DEFAULT 0,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    FOREIGN KEY (mesa_id) REFERENCES mesa(mesa_id),
    FOREIGN KEY (cliente_id) REFERENCES cliente(cliente_id),
    FOREIGN KEY (empleado_id) REFERENCES empleado(empleado_id)
);

-- Tabla de Detalles de Pedido
CREATE TABLE IF NOT EXISTS detalle_pedido (
    detalle_id INTEGER PRIMARY KEY AUTOINCREMENT,
    pedido_id INTEGER,
    producto_id INTEGER,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(pedido_id),
    FOREIGN KEY (producto_id) REFERENCES producto(producto_id)
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_producto_categoria ON producto(categoria_id);
CREATE INDEX IF NOT EXISTS idx_pedido_mesa ON pedido(mesa_id);
CREATE INDEX IF NOT EXISTS idx_pedido_cliente ON pedido(cliente_id);
CREATE INDEX IF NOT EXISTS idx_pedido_empleado ON pedido(empleado_id);
CREATE INDEX IF NOT EXISTS idx_detalle_pedido ON detalle_pedido(pedido_id);
CREATE INDEX IF NOT EXISTS idx_detalle_producto ON detalle_pedido(producto_id); 