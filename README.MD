¿Por qué necesitamos usar semáforos en el servidor si cada cliente está en una computadora diferente?

Porque ya que  aunque los clientes estén en máquinas distintas o aparte , los hilos ManejadorCliente viven todos en el mismo servidor. 
Cada vez que un cliente se conecta, el servidor crea un  hilo totalmente nuevo osea el ManejadorCliente que corre en su propia máquina. 
Todos esos hilos comparten las mismas variables estáticas en la memoria del servidoor. 
Sin sincronización, podrían modificar sillasOcupadas al mismo tiempo y causar por obvias razones varias condiciones de carrera, como dejar entrar más clientes de los que caben o perder actualizaciones del contador.