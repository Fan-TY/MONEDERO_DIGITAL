
function mostrarAlertaTransaccion(mensaje, tipo) {
    // Alerta básica nativa por ahora, luego la podemos tunear con Bootstrap
    console.log(`[Lukita Alerta] [${tipo.toUpperCase()}]: ${mensaje}`);
    alert(`${tipo === 'success' ? '✅' : '❌'} ${mensaje}`);
}