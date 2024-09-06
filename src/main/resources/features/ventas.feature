Feature: Gestión de ventas

  Scenario: Crear una venta exitosamente
    Given el producto con id 1 y stock de 10
    And la persona con id 1
    When se crea una venta con 2 unidades del producto
    Then la cantidad de stock del producto será 8

  Scenario: Intentar vender más stock del disponible
    Given el producto con id 1 y stock de 5
    And la persona con id 1
    When se intenta crear una venta con 10 unidades del producto
    Then ocurrirá un error indicando que el stock es insuficiente
