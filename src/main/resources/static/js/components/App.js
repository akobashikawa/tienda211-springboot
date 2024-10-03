import { ref, onMounted } from 'vue';
import axios from 'axios';
import { socket } from "../socket.js";
import config from '../config.js';
const apiBaseUrl = config.apiBaseUrl; console.log(apiBaseUrl)

// Obtener la URL base de la página actual
const baseUrl = apiBaseUrl || window.location.origin;

// Construir las URLs de los servicios
const productosServiceUrl = `${baseUrl}/api/productos`;
const personasServiceUrl = `${baseUrl}/api/personas`;
const ventasServiceUrl = `${baseUrl}/api/ventas`;

// Puedes usar estas URLs para tus peticiones, por ejemplo:
console.log('Productos URL:', productosServiceUrl);
console.log('Personas URL:', personasServiceUrl);
console.log('Ventas URL:', ventasServiceUrl);

const App = {
	template: `
	<h1>Tienda 211</h1>
			<div class="flash danger" v-if="errorMessage">{{ errorMessage }}</div>

			<h2>Eventos</h2>

			<div><ul><li v-for="evento of eventos">{{ evento }}</li></ul></div>

			<h2>Productos</h2>

			<button @click="getProductos()">Traer todos</button>
			<button @click="openIngresarProductoDialog">Ingresar Producto</button>

			<table>
				<thead>
					<tr>
						<th>id</th>
						<th>nombre</th>
						<th>costo</th>
						<th>precio</th>
						<th>cantidad</th>
						<th>acciones</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="producto of productos">
						<td>{{ producto.id }}</td>
						<td>{{ producto.nombre }}</td>
						<td>{{ producto.costo }}</td>
						<td>{{ producto.precio }}</td>
						<td>{{ producto.cantidad }}</td>
						<td>
							<button @click="openModificarProductoDialog(producto.id)">Modificar</button>
							<button @click="openIngresarVentaDialog({productoId: producto.id})">Vender</button>
						</td>
					</tr>
				</tbody>
			</table>

			<dialog ref="ingresarProductoDialog">
				<header>
					<h2>Ingresar Producto</h2>
				</header>
				<form method="dialog">
					<legend>Nuevo producto</legend>
					<fieldset>
						<label> Nombre: <input type="text" placeholder="" v-model="producto.nombre">
						</label> <label> Costo: <input type="number" step="0.10" min="0" placeholder="0.00"
								v-model="producto.costo">
						</label> <label> Precio: <input type="number" step="0.10" min="0" placeholder="0.00"
								v-model="producto.precio">
						</label> <label> Cantidad: <input type="number" step="1" min="0" placeholder="1"
								v-model="producto.cantidad">
						</label>
					</fieldset>
					<footer>
						<button type="reset" @click="closeIngresarProductoDialog">Cancelar</button>
						<button type="submit" @click="createProducto">Guardar</button>
					</footer>

				</form>
			</dialog>

			<dialog ref="modificarProductoDialog">
				<header>
					<h2>Modificar Producto</h2>
				</header>
				<form method="dialog">
					<fieldset>
						<legend>Producto</legend>
						<label> ID: {{ producto.id }} <input type="hidden" placeholder="" v-model="producto.id">
						</label> <label> Nombre: <input type="text" placeholder="" v-model="producto.nombre">
						</label> <label> Costo: <input type="number" step="0.10" min="0" placeholder="0.00"
								v-model="producto.costo">
						</label> <label> Precio: <input type="number" step="0.10" min="0" placeholder="0.00"
								v-model="producto.precio">
						</label> <label> Cantidad: <input type="number" step="1" min="0" placeholder="1"
								v-model="producto.cantidad">
						</label>
					</fieldset>
					<footer>
						<button type="submit" @click="updateProducto">Guardar</button>
						<button type="reset" @click="closeModificarProductoDialog">Cancelar</button>
					</footer>

				</form>
			</dialog>


			<h2>Personas</h2>

			<button @click="getPersonas()">Traer todas</button>
			<button @click="openIngresarPersonaDialog">Ingresar Persona</button>

			<table>
				<thead>
					<tr>
						<th>id</th>
						<th>nombre</th>
						<th>acciones</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="persona of personas">
						<td>{{ persona.id }}</td>
						<td>{{ persona.nombre }}</td>
						<td>
							<button @click="openModificarPersonaDialog(persona.id)">Modificar</button>
							<button @click="openIngresarVentaDialog({personaId: persona.id})">Venderle</button>
						</td>
					</tr>
				</tbody>
			</table>

			<dialog ref="ingresarPersonaDialog">
				<header>
					<h2>Ingresar Persona</h2>
				</header>
				<form method="dialog">
					<legend>Nueva persona</legend>
					<fieldset>
						<label> Nombre: <input type="text" placeholder="" v-model="persona.nombre">
						</label>
					</fieldset>
					<footer>
						<button type="reset" @click="closeIngresarPersonaDialog">Cancelar</button>
						<button type="submit" @click="createPersona">Guardar</button>
					</footer>

				</form>
			</dialog>

			<dialog ref="modificarPersonaDialog">
				<header>
					<h2>Modificar Persona</h2>
				</header>
				<form method="dialog">
					<fieldset>
						<legend>Persona</legend>
						<label> ID: {{ persona.id }} <input type="hidden" placeholder="" v-model="persona.id">
						</label> <label> Nombre: <input type="text" placeholder="" v-model="persona.nombre">
						</label>
					</fieldset>
					<footer>
						<button type="reset" @click="closeModificarPersonaDialog">Cancelar</button>
						<button type="submit" @click="updatePersona">Guardar</button>
					</footer>

				</form>
			</dialog>


			<h2>Ventas</h2>

			<button @click="getVentas()">Traer todas</button>
			<button @click="openIngresarVentaDialog()">Ingresar Venta</button>

			<table>
				<thead>
					<tr>
						<th>id</th>
						<th>fecha</th>
						<th>persona</th>
						<th>producto</th>
						<th>costo</th>
						<th>precio</th>
						<th>cantidad</th>
						<th>acciones</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="venta of ventas">
						<td>{{ venta.id }}</td>
						<td>{{ formatDate(venta.fechaHora) }}</td>
						<td>{{ venta.persona.nombre }}</td>
						<td>{{ venta.producto.nombre }}</td>
						<td>{{ venta.producto.costo }}</td>
						<td>{{ venta.precio }}</td>
						<td>{{ venta.cantidad }}</td>
						<td>
							<button @click="openModificarVentaDialog(venta.id)">Modificar</button>
						</td>
					</tr>
				</tbody>
			</table>

			<dialog ref="ingresarVentaDialog">
				<header>
					<h2>Ingresar Venta</h2>
				</header>
				<form method="dialog">
					<fieldset>
						<legend>Persona</legend>
						<label> <select v-model="venta.persona_id" @change="selectPersona">
								<option value="0">- Seleccionar -</option>
								<option :value="persona.id" v-for="persona of personas">{{
									persona.nombre }}</option>
							</select>
						</label>
					</fieldset>
					<fieldset>
						<legend>Producto</legend>
						<label> <select v-model="venta.producto_id" @change="selectProducto">
								<option value="0">- Seleccionar -</option>
								<option :value="producto.id" v-for="producto of productos">{{
									producto.nombre }}</option>
							</select>
						</label> <label> Precio: <input type="number" step="0.10" min="0" placeholder="0.00"
								v-model="venta.precio">
						</label> <label> Cantidad: <input type="number" step="1" min="0" placeholder="1"
								v-model="venta.cantidad">
						</label>
					</fieldset>
					<footer>
						<button type="reset" @click="closeIngresarVentaDialog">Cancelar</button>
						<button type="submit" @click="createVenta">Vender</button>
					</footer>

				</form>
			</dialog>

			<dialog ref="modificarVentaDialog">
				<header>
					<h2>Modificar Venta</h2>
				</header>
				<form method="dialog">
					<fieldset>
						<legend>Persona</legend>
						<label> <select v-model="venta.persona_id" @change="selectPersona">
								<option value="0">- Seleccionar -</option>
								<option :value="persona.id" v-for="persona of personas">{{
									persona.nombre }}</option>
							</select>
						</label>
					</fieldset>
					<fieldset>
						<legend>Producto</legend>
						<label> <select v-model="venta.producto_id" @change="selectProducto">
								<option value="0">- Seleccionar -</option>
								<option :value="producto.id" v-for="producto of productos">{{
									producto.nombre }}</option>
							</select>
						</label> <label> Precio: <input type="number" step="0.10" min="0" placeholder="0.00"
								v-model="venta.precio">
						</label> <label> Cantidad: <input type="number" step="1" min="0" placeholder="1"
								v-model="venta.cantidad">
						</label>
					</fieldset>
					<footer>
						<button type="reset" @click="closeModificarVentaDialog">Cancelar</button>
						<button type="submit" @click="updateVenta">Guardar</button>
					</footer>

				</form>
			</dialog>

			
			<footer>
				<em><a href="https://github.com/akobashikawa/tienda211-springboot" target="_blank">Tienda 211 - Spring Boot</a></em>
			</footer>
    `,

	setup() {
		// UTILS
		let errorMessage = ref(false);
		const maxEventos = 5;

		const formatDate = (date) => {
			const d = new Date(date);
			const year = d.getFullYear();
			const month = String(d.getMonth() + 1).padStart(2, '0');
			const day = String(d.getDate()).padStart(2, '0');
			const hours = String(d.getHours()).padStart(2, '0');
			const minutes = String(d.getMinutes()).padStart(2, '0');

			return `${year}/${month}/${day} ${hours}:${minutes}`;
		};

		// EVENTOS
		const eventos = ref([]);
		let numEvento = 0;

		const addEvento = (evento) => {
			numEvento++;
			eventos.value.push(`${numEvento}. ${evento}`);
			// Verificar si la longitud del array supera el límite
			if (eventos.value.length > maxEventos) {
				// Eliminar el primer elemento para mantener el tamaño limitado
				eventos.value.shift();
			}
		};


		// LISTAR PRODUCTOS
		let productos = ref([]);

		const getProductos = async () => {
			const { data } = await axios.get(productosServiceUrl);
			productos.value = data;
		};

		const getProducto = async (id) => {
			const { data } = await axios.get(`${productosServiceUrl}/${id}`);
			producto.value = data;
		};

		const selectProducto = () => {
			const producto_id = venta.value.producto_id;
			const found = productos.value.find(item => item.id === producto_id);
			if (found) {
				venta.value.precio = found.precio;
			}
		};

		// CREAR PRODUCTO
		const producto = ref({
			id: 0,
			nombre: '',
			costo: 0,
			precio: 0,
			cantidad: 1,
		});

		const ingresarProductoDialog = ref();
		const openIngresarProductoDialog = () => {
			ingresarProductoDialog.value.showModal();
		};
		const closeIngresarProductoDialog = () => {
			ingresarProductoDialog.value.close();
		};

		const createProducto = async () => {
			const body = producto.value;
			delete body.id;
			const { data } = await axios.post(productosServiceUrl, body);
			// await getProductos();
		};

		// MODIFICAR PRODUCTO
		const modificarProductoDialog = ref();
		const openModificarProductoDialog = async (id) => {
			await getProducto(id);
			modificarProductoDialog.value.showModal();
		};
		const closeModificarProductoDialog = () => {
			modificarProductoDialog.value.close();
		};

		const updateProducto = async () => {
			const body = producto.value;
			const { data } = await axios.put(`${productosServiceUrl}/${body.id}`, body);
			// await getProductos();
		};

		// LISTAR PERSONAS
		let personas = ref([]);

		const getPersonas = async () => {
			const { data } = await axios.get(personasServiceUrl);
			personas.value = data;
		};

		const getPersona = async (id) => {
			const { data } = await axios.get(`${personasServiceUrl}/${id}`);
			persona.value = data;
		};

		const selectPersona = () => {
			const persona_id = venta.value.persona_id;
			const found = personas.value.find(item => item.id === persona_id);
			if (found) {
				venta.value.nombre = found.nombre;
			}
		};

		// CREAR PERSONA
		const persona = ref({
			id: 0,
			nombre: '',
		});

		const ingresarPersonaDialog = ref();
		const openIngresarPersonaDialog = () => {
			ingresarPersonaDialog.value.showModal();
		};
		const closeIngresarPersonaDialog = () => {
			ingresarPersonaDialog.value.close();
		};

		const createPersona = async () => {
			const body = persona.value;
			const { data } = await axios.post(personasServiceUrl, body);
			// await getPersonas();
		};

		// MODIFICAR PERSONA
		const modificarPersonaDialog = ref();
		const openModificarPersonaDialog = async (id) => {
			await getPersona(id);
			modificarPersonaDialog.value.showModal();
		};
		const closeModificarPersonaDialog = () => {
			modificarPersonaDialog.value.close();
		};

		const updatePersona = async () => {
			const body = persona.value;
			const { data } = await axios.put(`${personasServiceUrl}/${body.id}`, body);
			// await getPersonas();
		};

		// CREAR VENTA
		const venta = ref({
			persona_id: 0,
			producto_id: 0,
			precio: 0,
			cantidad: 1,
		});

		const createVenta = async () => {
			const body = venta.value;
			try {
				const { data } = await axios.post(ventasServiceUrl, body);
			} catch (error) {
				errorMessage.value = error.response.data ? error.response.data.error : false;
			} finally {
				// await getVentas();
				// await getProductos();
			}
		};

		// LISTAR VENTAS
		let ventas = ref([]);

		const getVentas = async () => {
			const { data } = await axios.get(ventasServiceUrl);
			ventas.value = data;
		};

		const getVenta = async (id) => {
			const { data } = await axios.get(`${ventasServiceUrl}/${id}`);
			venta.value = data;
			venta.value.producto_id = venta.value.producto.id;
			venta.value.persona_id = venta.value.persona.id;
		};

		// INGRESAR VENTA
		const ingresarVentaDialog = ref();
		const openIngresarVentaDialog = async ({ personaId, productoId } = {}) => {
			if (personaId) {
				await getPersona(personaId);
				venta.value = Object.assign(venta.value, {
					persona_id: persona.value.id,
				});
			}
			if (productoId) {
				await getProducto(productoId);
				venta.value = Object.assign(venta.value, {
					producto_id: producto.value.id,
					precio: producto.value.precio,
					cantidad: 1,
				});
			}
			ingresarVentaDialog.value.showModal();
		};
		const closeIngresarVentaDialog = () => {
			ingresarVentaDialog.value.close();
		};

		// MODIFICAR VENTA
		const modificarVentaDialog = ref();
		const openModificarVentaDialog = async (id) => {
			await getVenta(id);
			modificarVentaDialog.value.showModal();
		};
		const closeModificarVentaDialog = () => {
			modificarVentaDialog.value.close();
		};

		const updateVenta = async () => {
			const body = venta.value;
			const { data } = await axios.put(`${ventasServiceUrl}/${body.id}`, body);
			// await getVentas();
			// await getProductos();
		};


		onMounted(async () => {
			await getProductos();
			await getPersonas();
			await getVentas();

			socket.on('productoCreated', async (created) => {
				addEvento('productoCreated');
				console.log('productoCreated', created);
				await getProductos();
			});

			socket.on('productoUpdated', async (updated) => {
				addEvento('productoUpdated');
				console.log('productoUpdated', updated);
				await getProductos();
			});


			socket.on('personaCreated', async (created) => {
				addEvento('personaCreated');
				console.log('personaCreated', created);
				await getPersonas();
			});

			socket.on('personaUpdated', async (updated) => {
				addEvento('personaUpdated');
				console.log('personaUpdated', updated);
				await getPersonas();
			});


			socket.on('ventaCreated', async (created) => {
				addEvento('ventaCreated');
				console.log('ventaCreated', created);
				await getVentas();
				await getProductos();
			});

			socket.on('ventaUpdated', async (updated) => {
				addEvento('ventaUpdated');
				console.log('ventaUpdated', updated);
				await getVentas();
				await getProductos();
			});
		});

		socket.on('productoActualizado', (productoActualizado) => {
			const index = productos.value.findIndex(p => p.id === productoActualizado.id);
			if (index !== -1) {
				productos.value.splice(index, 1, productoActualizado);
			} else {
				productos.value.push(productoActualizado);
			}
		});

		return {
			errorMessage,
			eventos,
			formatDate,

			productos,
			producto,
			getProductos,
			selectProducto,
			ingresarProductoDialog,
			openIngresarProductoDialog,
			closeIngresarProductoDialog,
			createProducto,
			modificarProductoDialog,
			openModificarProductoDialog,
			closeModificarProductoDialog,
			updateProducto,

			personas,
			persona,
			getPersonas,
			selectPersona,
			ingresarPersonaDialog,
			openIngresarPersonaDialog,
			closeIngresarPersonaDialog,
			createPersona,
			modificarPersonaDialog,
			openModificarPersonaDialog,
			closeModificarPersonaDialog,
			updatePersona,

			ventas,
			venta,
			getVentas,
			getVenta,
			createVenta,
			ingresarVentaDialog,
			openIngresarVentaDialog,
			closeIngresarVentaDialog,
			modificarVentaDialog,
			openModificarVentaDialog,
			closeModificarVentaDialog,
			updateVenta,
		}
	},

};

export default App;