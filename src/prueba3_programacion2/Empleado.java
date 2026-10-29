
package prueba3_programacion2;

public abstract class Empleado implements Cloneable {
    private String codigo;
    private String nombre;
    private double salarioBase;
    public Empleado(String codigo, String nombre, double salarioBase){
        setCodigo(codigo);
        setNombre(nombre);
        setSalarioBase(salarioBase);
    }
    
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()){
            throw new IllegalArgumentException("El codigo no puede estar vacio");
        }
        this.codigo = codigo;
    }
    
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        this.nombre = nombre;
    }
    
    public void setSalarioBase(double salarioBase){
        if (salarioBase < 0) {
            throw new IllegalArgumentException("El salario base no puede ser negativo");
        }
        this.salarioBase = salarioBase;
    }
    
    public String getCodigo(){
        return codigo;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public double getSalarioBase(){
        return salarioBase;
    }
    
    public abstract double calcularSalario();

    public String obtenerCategoria(){
        double salario = calcularSalario();
        if (salario > 2000){
            return "Superior";
        } else if (salario > 1000){
            return "Estandar";
        } else {
            return "Basico";
        }
    }

    @Override
    public Empleado clone() {
        try {
            return (Empleado) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
