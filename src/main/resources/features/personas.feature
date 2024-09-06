Feature: Gestión de personas

  Scenario: Crear una nueva persona exitosamente
    Given una persona con nombre "Juan Pérez"
    When se crea la persona
    Then la persona será guardada exitosamente

  Scenario: Actualizar una persona existente
    Given una persona existente con id 1 y nombre "Juan Pérez"
    When se actualiza el nombre a "Ana Pérez"
    Then la persona tendrá el nombre "Ana Pérez"

  Scenario: Intentar eliminar una persona que no existe
    Given no existe una persona con id 999
    When se intenta eliminar la persona
    Then ocurrirá un error indicando que la persona no fue encontrada
