
Transporte Tonito – Consola (Java)
==================================
Requisitos: Java 17+ (o 11+).

Ejecución:
  java -cp out ui.Main

Notas:
- Todos los datos se ingresan por teclado con validaciones básicas.
- "Validar" de empleados y vehículos imprime datos y confirma OK/errores.
- RTV: se considera anual. Service: cada 10.000 km desde el último realizado.
- Carnet: B (camioneta), C (camión SIN trailer y camioneta), E (todos).
- Salarios: Administrativo A = $1000 y +$150 por letra adicional. Chofer: $800 + $0.10 por km acumulado.
- Subordinados: sólo administrativos categoría E o superior. Se impide que un subordinado tenga múltiples supervisores y se evitan ciclos jerárquicos.
