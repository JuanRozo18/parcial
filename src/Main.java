import java.util.Scanner;

public class Main {
    private static List<Producto> inventario = new ArrayList<>();
    private static List<Cliente> clientes = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarInventario();
        int opcion;
        do {
            System.out.println("\n--- Menú de opciones ---");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Comprar producto");
            System.out.println("3. Mostrar compras de un cliente");
            System.out.println("4. Mostrar inventario");
            System.out.println("5. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre del cliente: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Ingrese el correo del cliente: ");
                    String correo = scanner.nextLine();
                    clientes.add(new Cliente(nombre, correo));
                    System.out.println("Cliente registrado exitosamente.");
                    break;

                case 2:
                    System.out.print("Ingrese el nombre del cliente: ");
                    String nombreCliente = scanner.nextLine();
                    Cliente cliente = buscarCliente(nombreCliente);

                    if (cliente == null) {
                        System.out.println("Cliente no encontrado.");
                        return;
                    }

                    mostrarInventario();
                    System.out.print("Ingrese el número del producto a comprar: ");
                    int productoIndex = scanner.nextInt() - 1;
                    if (productoIndex < 0 || productoIndex >= inventario.size()) {
                        System.out.println("Producto no válido.");
                        return;
                    }

                    Producto producto = inventario.get(productoIndex);
                    System.out.print("Ingrese la cantidad a comprar: ");
                    int cantidad = scanner.nextInt();
                    scanner.nextLine();

                    cliente.comprarProducto(producto, cantidad);
                    System.out.println("Compra realizada.");
                    break;

                case 3:
                    System.out.print("Ingrese el nombre del cliente: ");
                    String nombreCliente = scanner.nextLine();
                    Cliente cliente = buscarCliente(nombreCliente);

                    if (cliente == null) {
                        System.out.println("Cliente no encontrado.");
                    } else {
                        cliente.mostrarCompra();
                    }
                    break;

                case 4:
                    System.out.println("\n--- Inventario de Productos ---");
                    for (int i = 0; i < inventario.size(); i++) {
                        System.out.print((i + 1) + ". ");
                        inventario.get(i).mostrarDetalles();
                    }
                    break:

                case 5:
                    System.out.println("Saliendo del sistema...");
                    break;


                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
    }

    private static void inicializarInventario() {
        inventario.add(new Laptop("MacBook Air", "Apple", 1200, 10, "M1", 8));
        inventario.add(new Celular("Galaxy S21", "Samsung", 800, 15, 4000, 64));
    }


    private static Cliente buscarCliente(String nombre) {
        for (Cliente cliente : clientes) {
            if (cliente.nombre.equalsIgnoreCase(nombre)) {
                return cliente;
            }
        }
        return null;
    }


}


abstract class Producto {
    protected String nombre;
    protected String marca;
    protected double precio;
    protected int cantidadStock;

    public Producto(String nombre, String marca, double precio, int cantidadStock) {
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.cantidadStock = cantidadStock;
    }

    public abstract void mostrarDetalles();

    public int getCantidadStock() {
        return cantidadStock;
    }

    public void reducirStock(int cantidad) {
        this.cantidadStock -= cantidad;
    }

    public double getPrecio() {
        return precio;
    }
}

interface Vendible {
    double calcularPrecioVenta(int cantidad);
}

class Laptop extends Producto implements Vendible {
    private String procesador;
    private int memoriaRAM;

    public Laptop(String nombre, String marca, double precio, int cantidadStock, String procesador, int memoriaRAM) {
        super(nombre, marca, precio, cantidadStock);
        this.procesador = procesador;
        this.memoriaRAM = memoriaRAM;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Laptop: " + nombre + ", Marca: " + marca + ", Procesador: " + procesador +
                ", RAM: " + memoriaRAM + "GB, Precio: $" + precio + ", Stock: " + cantidadStock);
    }

    @Override
    public double calcularPrecioVenta(int cantidad) {
        double total = cantidad * precio;
        if (cantidad > 5) {
            total *= 0.9;
        }
        return total;
    }
}

class Celular extends Producto implements Vendible {
    private int capacidadBateria;
    private double camaraResolucion;

    public Celular(String nombre, String marca, double precio, int cantidadStock, int capacidadBateria, double camaraResolucion) {
        super(nombre, marca, precio, cantidadStock);
        this.capacidadBateria = capacidadBateria;
        this.camaraResolucion = camaraResolucion;
    }

    @Override
    public void mostrarDetalles() {
        System.out.println("Celular: " + nombre + ", Marca: " + marca + ", Batería: " + capacidadBateria + "mAh, Cámara: " +
                camaraResolucion + "MP, Precio: $" + precio + ", Stock: " + cantidadStock);
    }

    @Override
    public double calcularPrecioVenta(int cantidad) {
        double total = cantidad * precio;
        if (cantidad > 5) {
            total *= 0.95;
        }
        return total;
    }
}

class Cliente {
    private String nombre;
    private String correo;
    private List<Producto> productosComprados = new ArrayList<>();
    private List<Integer> cantidadesCompradas = new ArrayList<>();

    public Cliente(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    public void comprarProducto(Producto producto, int cantidad) {
        if (cantidad > producto.getCantidadStock()) {
            System.out.println("No hay suficiente stock para " + producto.nombre);
            return;
        }
        productosComprados.add(producto);
        cantidadesCompradas.add(cantidad);
        producto.reducirStock(cantidad);
    }

    public void mostrarCompra() {
        System.out.println("Cliente: " + nombre + " - " + correo);
        double totalCompra = 0;
        for (int i = 0; i < productosComprados.size(); i++) {
            Producto producto = productosComprados.get(i);
            int cantidad = cantidadesCompradas.get(i);
            double precioVenta = ((Vendible) producto).calcularPrecioVenta(cantidad);
            totalCompra += precioVenta;
            producto.mostrarDetalles();
            System.out.println("Cantidad: " + cantidad + ", Costo: $" + precioVenta);
        }
        System.out.println("Total de la compra: $" + totalCompra);
    }
}