
package prueba3_programacion2;

public class EmpleadoPorHoras extends Empleado{
    private int horasTrabajadas;
    private double tarifaPorHora;

    public EmpleadoPorHoras(String codigo, String nombre, double salarioBase, int horasTrabajadas, double tarifaPorHora) {
        super(codigo, nombre, salarioBase);
        setHorasTrabajadas(horasTrabajadas);
        setTarifaPorHora(tarifaPorHora);
    }
    
    public void setHorasTrabajadas(int horasTrabajadas) {
        if (horasTrabajadas < 0) {
            throw new IllegalArgumentException("Las horas trabajadas no pueden ser negativas");
        }
        this.horasTrabajadas = horasTrabajadas;
    }
    
    public void setTarifaPorHora(double tarifaPorHora) {
        if (tarifaPorHora < 0) {
            throw new IllegalArgumentException("La tarifa por hora no puede ser negativa");
        }
        this.tarifaPorHora = tarifaPorHora;
    }
    
    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }
    
    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    @Override
    public double calcularSalario() {
        return getSalarioBase() + (horasTrabajadas * tarifaPorHora);
    }
    
}
