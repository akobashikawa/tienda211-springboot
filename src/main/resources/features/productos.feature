Feature: Gestión de productos

  Scenario: Crear un nuevo producto exitosamente
    Given un producto con nombre "Producto A" y costo 50 y precio 100 y cantidad 10
    When se crea el producto
    Then el producto será guardado exitosamente

  Scenario: Actualizar un producto existente
    Given un producto existente con id 1 y nombre "Producto A" y costo 50 y precio 100 y cantidad 10
    When se actualiza el nombre a "Producto B" y el precio a 150
    Then el producto tendrá el nombre "Producto B" y precio 150

  Scenario: Intentar eliminar un producto que no existe
    Given no existe un producto con id 999
    When se intenta eliminar el producto
    Then ocurrirá un error indicando que el producto no fue encontrado
