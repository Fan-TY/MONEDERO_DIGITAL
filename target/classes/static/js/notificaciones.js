let stompClient = null;

let contador = 0;

function conectarNotificaciones() {

    const usuarioId = document.getElementById("usuarioId").value;

    const socket = new SockJS("/ws");

    stompClient = Stomp.over(socket);

    stompClient.debug = null;

    stompClient.connect({}, function () {

        console.log("Conectado al WebSocket");

        stompClient.subscribe("/topic/notificaciones/" + usuarioId, function (mensaje) {

            const notificacion = typeof mensaje.body === 'string' ? JSON.parse(mensaje.body) : mensaje.body;
            mostrarNotificacion(notificacion);

        });

    });

}

function mostrarNotificacion(notificacion){

    const lista = document.getElementById("listaNotificaciones");

    const vacio = document.getElementById("sinNotificaciones");

    if(vacio){

        vacio.remove();

    }

    contador++;

    const badge = document.getElementById("contadorNotificaciones");

    badge.style.display="inline";

    badge.innerText=contador;

    const li=document.createElement("li");

    li.innerHTML=`

        <a class="dropdown-item">

            <strong>${notificacion.tipo}</strong>

            <br>

            <small>${notificacion.mensaje}</small>

        </a>

    `;

    lista.prepend(li);

}