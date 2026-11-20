/* ==========================================================
   FRIIADMI · main.js
   Solo interacción del navegador: buscador, modales y validaciones.
   La lógica de negocio (pagos, puntos, premios) vive en Java.
   ========================================================== */
(function () {
    'use strict';

    var PUNTOS_POR_POSICION = [12, 9, 8, 7, 6, 5, 4, 3, 2, 1];

    function $(selector, contexto) { return (contexto || document).querySelector(selector); }
    function $$(selector, contexto) { return Array.prototype.slice.call((contexto || document).querySelectorAll(selector)); }
    function porId(id) { return document.getElementById(id); }

    function normalizar(texto) {
        return (texto || '').toString().normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();
    }

    function hoyISO() {
        var d = new Date();
        var mes = String(d.getMonth() + 1).padStart(2, '0');
        var dia = String(d.getDate()).padStart(2, '0');
        return d.getFullYear() + '-' + mes + '-' + dia;
    }

    /* ---------------------- Foto de perfil ---------------------- */
    window.verFotoPerfil = function (ruta) {
        var img = porId('vistaFotoPerfil');
        if (!img) { return; }
        if (ruta) { img.src = ruta; img.style.display = 'block'; }
        else { img.style.display = 'none'; }
    };

    /* ---------------------- Modales ---------------------- */
    function abrirModal(id) {
        var modal = porId(id);
        if (!modal) { return; }
        modal.classList.add('open');
        modal.setAttribute('aria-hidden', 'false');
        document.body.classList.add('modal-open');
        var primero = $('input:not([type=hidden]):not([disabled]), select', modal);
        if (primero) { setTimeout(function () { primero.focus(); }, 40); }
    }

    function cerrarModal(modal) {
        if (!modal) { return; }
        modal.classList.remove('open');
        modal.setAttribute('aria-hidden', 'true');
        if (!$('.modal.open')) { document.body.classList.remove('modal-open'); }
    }

    document.addEventListener('click', function (e) {
        var cerrar = e.target.closest('[data-close-modal]');
        if (cerrar) { cerrarModal(cerrar.closest('.modal')); return; }
        if (e.target.classList && e.target.classList.contains('modal')) { cerrarModal(e.target); }
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') { $$('.modal.open').forEach(cerrarModal); }
    });

    /* ---------------------- Confirmación de acciones ---------------------- */
    var formPendiente = null;

    document.addEventListener('submit', function (e) {
        var form = e.target;
        if (form.dataset && form.dataset.confirm && !form.dataset.confirmado) {
            e.preventDefault();
            formPendiente = form;
            var mensaje = porId('confirmMensaje');
            if (mensaje) { mensaje.textContent = form.dataset.confirm; }
            abrirModal('modalConfirm');
        }
    });

    var botonConfirmar = porId('confirmAceptar');
    if (botonConfirmar) {
        botonConfirmar.addEventListener('click', function () {
            if (!formPendiente) { return; }
            var form = formPendiente;
            formPendiente = null;
            form.dataset.confirmado = '1';
            cerrarModal(porId('modalConfirm'));
            form.submit();
        });
    }

    /* ---------------------- Alertas: se ocultan solas ---------------------- */
    $$('.alert-success').forEach(function (alerta) {
        setTimeout(function () {
            alerta.style.opacity = '0';
            setTimeout(function () { alerta.remove(); }, 450);
        }, 6000);
    });

    /* ---------------------- Buscador y filtro de tablas ---------------------- */
    function filtrarTabla(idTabla) {
        var tabla = porId(idTabla);
        if (!tabla) { return; }
        var buscador = $('#buscador');
        var filtro = $('#filtroEstado');
        var consulta = normalizar(buscador ? buscador.value : '');
        var estado = filtro ? filtro.value : '';
        var visibles = 0;

        $$('tbody tr[data-filtrable]', tabla).forEach(function (fila) {
            var coincideTexto = normalizar(fila.textContent).indexOf(consulta) !== -1;
            var coincideEstado = !estado || fila.dataset.estado === estado;
            var mostrar = coincideTexto && coincideEstado;
            fila.hidden = !mostrar;
            if (mostrar) { visibles++; }
        });

        var vacio = porId('sinResultados');
        if (vacio) {
            var hayFilas = $$('tbody tr[data-filtrable]', tabla).length > 0;
            vacio.hidden = !(hayFilas && visibles === 0);
        }
    }

    ['buscador', 'filtroEstado'].forEach(function (id) {
        var control = porId(id);
        if (!control) { return; }
        control.addEventListener(id === 'buscador' ? 'input' : 'change', function () {
            filtrarTabla(control.dataset.tabla);
        });
    });

    /* ---------------------- Validaciones: utilidades ---------------------- */
    function limpiarErrores(form) {
        $$('.field-error', form).forEach(function (el) { el.remove(); });
        $$('.invalid', form).forEach(function (el) { el.classList.remove('invalid'); });
    }

    function marcarError(campo, mensaje) {
        campo.classList.add('invalid');
        var aviso = document.createElement('small');
        aviso.className = 'field-error';
        aviso.textContent = mensaje;
        var contenedor = campo.closest('.field') || campo.parentNode;
        contenedor.appendChild(aviso);
    }

    function enfocarPrimerError(form) {
        var primero = $('.invalid', form);
        if (primero) { primero.focus(); }
    }

    /* ---------------------- Jugadores ---------------------- */
    var formJugador = porId('formJugador');
    if (formJugador) {
        var campos = {
            id: porId('jugadorId'), nombre: porId('jugadorNombre'), idff: porId('jugadorIdFF'),
            celular: porId('jugadorCelular'), equipo: porId('jugadorEquipo'), capitan: porId('jugadorCapitan'),
            monto: porId('jugadorMonto'), estado: porId('jugadorEstado'), fecha: porId('jugadorFecha'),
            metodo: porId('jugadorMetodo'), comprobante: porId('jugadorComprobante'), foto: porId('jugadorFoto')
        };

        var sincronizarCapitan = function () {
            var tieneEquipo = campos.equipo.value !== '';
            campos.capitan.disabled = !tieneEquipo;
            if (!tieneEquipo) { campos.capitan.checked = false; }
        };

        campos.equipo.addEventListener('change', sincronizarCapitan);

        campos.estado.addEventListener('change', function () {
            if (campos.estado.value === 'PAGADO' && !campos.fecha.value) { campos.fecha.value = hoyISO(); }
        });

        porId('btnNuevoJugador').addEventListener('click', function (e) {
            formJugador.reset();
            limpiarErrores(formJugador);
            campos.id.value = '';
            campos.monto.value = e.currentTarget.dataset.cuota || '';
            campos.foto.value = '';
            window.verFotoPerfil('');
            porId('tituloModalJugador').textContent = 'Nuevo jugador';
            porId('hintComprobante').textContent = 'Imagen o PDF de hasta 5 MB.';
            sincronizarCapitan();
            abrirModal('modalJugador');
        });

        $$('.btn-editar-jugador').forEach(function (boton) {
            boton.addEventListener('click', function () {
                var d = boton.dataset;
                formJugador.reset();
                limpiarErrores(formJugador);
                campos.id.value = d.id || '';
                campos.nombre.value = d.nombre || '';
                campos.idff.value = d.idff || '';
                campos.celular.value = d.celular || '';
                campos.equipo.value = d.equipo || '';
                campos.capitan.checked = d.capitan === 'true';
                campos.monto.value = d.monto || '';
                campos.estado.value = d.estado || 'PENDIENTE';
                campos.fecha.value = d.fecha || '';
                campos.metodo.value = d.metodo || '';
                campos.foto.value = d.foto || '';
                window.verFotoPerfil(d.foto || '');
                porId('tituloModalJugador').textContent = 'Editar jugador';
                porId('hintComprobante').textContent = d.comprobante === 'true'
                    ? 'Ya hay un comprobante guardado. Sube otro solo si quieres reemplazarlo.'
                    : 'Imagen o PDF de hasta 5 MB.';
                sincronizarCapitan();
                abrirModal('modalJugador');
            });
        });

        formJugador.addEventListener('submit', function (e) {
            limpiarErrores(formJugador);
            if (campos.nombre.value.trim().length < 3) {
                marcarError(campos.nombre, 'Escribe el nombre completo del jugador.');
            }
            if (!/^\d{6,15}$/.test(campos.idff.value.trim())) {
                marcarError(campos.idff, 'El ID debe tener entre 6 y 15 dígitos.');
            }
            if (!/^9\d{8}$/.test(campos.celular.value.trim())) {
                marcarError(campos.celular, 'Usa 9 dígitos y que empiece con 9.');
            }
            if (campos.monto.value === '' || parseFloat(campos.monto.value) < 0) {
                marcarError(campos.monto, 'Ingresa un monto igual o mayor a 0.');
            }
            var archivo = campos.comprobante.files && campos.comprobante.files[0];
            if (archivo && archivo.size > 5 * 1024 * 1024) {
                marcarError(campos.comprobante, 'El comprobante no puede superar 5 MB.');
            }
            if ($('.invalid', formJugador)) {
                e.preventDefault();
                enfocarPrimerError(formJugador);
            }
        });

        sincronizarCapitan();
    }

    /* ---------------------- Equipos ---------------------- */
    var formEquipo = porId('formEquipo');
    if (formEquipo) {
        var equipoId = porId('equipoId');
        var equipoNombre = porId('equipoNombre');
        var selectCapitan = porId('equipoCapitan');
        var casillas = $$('input[name="integrantesIds"]', formEquipo);
        var contador = porId('contadorIntegrantes');

        var sincronizarEquipo = function () {
            var actual = equipoId.value;
            var capitan = selectCapitan.value;

            $$('option', selectCapitan).forEach(function (op) {
                if (!op.value) { return; }
                var ocupado = !!op.dataset.equipo && op.dataset.equipo !== actual;
                op.disabled = ocupado;
            });

            casillas.forEach(function (casilla) {
                var ocupado = !!casilla.dataset.equipo && casilla.dataset.equipo !== actual;
                var esCapitan = capitan !== '' && casilla.value === capitan;
                if (esCapitan) { casilla.checked = false; }
                casilla.disabled = ocupado || esCapitan;
                var etiqueta = casilla.closest('label');
                if (etiqueta) { etiqueta.classList.toggle('disabled', casilla.disabled); }
            });

            var marcados = casillas.filter(function (c) { return c.checked; }).length;
            contador.textContent = marcados + '/4';
        };

        selectCapitan.addEventListener('change', sincronizarEquipo);

        casillas.forEach(function (casilla) {
            casilla.addEventListener('change', function () {
                var marcados = casillas.filter(function (c) { return c.checked; }).length;
                if (marcados > 4) {
                    casilla.checked = false;
                    limpiarErrores(formEquipo);
                    marcarError(contador.closest('fieldset'), 'Un equipo tiene un capitán y como máximo 4 jugadores más.');
                }
                sincronizarEquipo();
            });
        });

        porId('btnNuevoEquipo').addEventListener('click', function () {
            formEquipo.reset();
            limpiarErrores(formEquipo);
            equipoId.value = '';
            porId('tituloModalEquipo').textContent = 'Nuevo equipo';
            sincronizarEquipo();
            abrirModal('modalEquipo');
        });

        $$('.btn-editar-equipo').forEach(function (boton) {
            boton.addEventListener('click', function () {
                var d = boton.dataset;
                formEquipo.reset();
                limpiarErrores(formEquipo);
                equipoId.value = d.id || '';
                equipoNombre.value = d.nombre || '';
                selectCapitan.value = d.capitan || '';
                var ids = (d.integrantes || '').split(',').filter(Boolean);
                casillas.forEach(function (casilla) { casilla.checked = ids.indexOf(casilla.value) !== -1; });
                porId('tituloModalEquipo').textContent = 'Editar equipo';
                sincronizarEquipo();
                abrirModal('modalEquipo');
            });
        });

        formEquipo.addEventListener('submit', function (e) {
            limpiarErrores(formEquipo);
            if (equipoNombre.value.trim().length < 2) {
                marcarError(equipoNombre, 'Escribe el nombre del equipo.');
            }
            if (selectCapitan.value === '') {
                marcarError(selectCapitan, 'Elige al capitán del equipo.');
            }
            if ($('.invalid', formEquipo)) {
                e.preventDefault();
                enfocarPrimerError(formEquipo);
            }
        });

        sincronizarEquipo();
    }

    /* ---------------------- Partidas y resultados ---------------------- */
    var formPartida = porId('formPartida');
    if (formPartida) {
        porId('btnNuevaPartida').addEventListener('click', function () {
            limpiarErrores(formPartida);
            abrirModal('modalPartida');
        });

        formPartida.addEventListener('submit', function (e) {
            limpiarErrores(formPartida);
            if (porId('partidaNombre').value.trim() === '') {
                marcarError(porId('partidaNombre'), 'Escribe el nombre de la partida.');
            }
            if (porId('partidaFecha').value === '') {
                marcarError(porId('partidaFecha'), 'Elige la fecha de la partida.');
            }
            if ($('.invalid', formPartida)) {
                e.preventDefault();
                enfocarPrimerError(formPartida);
            }
        });
    }

    var formResultado = porId('formResultado');
    if (formResultado) {
        var selPartida = porId('resultadoPartida');
        var selEquipo = porId('resultadoEquipo');
        var inPosicion = porId('resultadoPosicion');
        var inKills = porId('resultadoKills');
        var vistaPuntos = porId('previewPuntos');

        var actualizarVistaPuntos = function () {
            var posicion = parseInt(inPosicion.value, 10);
            var kills = parseInt(inKills.value, 10);
            if (isNaN(posicion) || posicion < 1 || posicion > 12) { vistaPuntos.textContent = '—'; return; }
            if (isNaN(kills) || kills < 0) { kills = 0; }
            vistaPuntos.textContent = String((PUNTOS_POR_POSICION[posicion - 1] || 0) + kills);
        };

        var abrirResultado = function (partidaId) {
            formResultado.reset();
            limpiarErrores(formResultado);
            if (partidaId) { selPartida.value = partidaId; }
            actualizarVistaPuntos();
            abrirModal('modalResultado');
        };

        porId('btnNuevoResultado').addEventListener('click', function () { abrirResultado(''); });
        $$('.btn-resultado-partida').forEach(function (boton) {
            boton.addEventListener('click', function () { abrirResultado(boton.dataset.partida); });
        });

        inPosicion.addEventListener('input', actualizarVistaPuntos);
        inKills.addEventListener('input', actualizarVistaPuntos);

        formResultado.addEventListener('submit', function (e) {
            limpiarErrores(formResultado);
            var posicion = parseInt(inPosicion.value, 10);
            var kills = parseInt(inKills.value, 10);
            if (selPartida.value === '') { marcarError(selPartida, 'Elige la partida.'); }
            if (selEquipo.value === '') { marcarError(selEquipo, 'Elige el equipo.'); }
            if (isNaN(posicion) || posicion < 1 || posicion > 12) {
                marcarError(inPosicion, 'La posición va de 1 a 12.');
            }
            if (isNaN(kills) || kills < 0 || kills > 99) {
                marcarError(inKills, 'Las kills van de 0 a 99.');
            }
            if ($('.invalid', formResultado)) {
                e.preventDefault();
                enfocarPrimerError(formResultado);
            }
        });
    }

    /* ---------------------- Configuración del torneo ---------------------- */
    var formConfig = porId('formConfig');
    if (formConfig) {
        formConfig.addEventListener('submit', function (e) {
            limpiarErrores(formConfig);
            var comision = parseFloat(porId('cfgComision').value);
            var cuota = parseFloat(porId('cfgCuota').value);
            if (porId('cfgNombre').value.trim() === '') {
                marcarError(porId('cfgNombre'), 'Escribe el nombre del torneo.');
            }
            if (isNaN(comision) || comision < 0 || comision > 100) {
                marcarError(porId('cfgComision'), 'La comisión va de 0 a 100.');
            }
            if (isNaN(cuota) || cuota < 0) {
                marcarError(porId('cfgCuota'), 'Ingresa una cuota igual o mayor a 0.');
            }
            if ($('.invalid', formConfig)) {
                e.preventDefault();
                enfocarPrimerError(formConfig);
            }
        });
    }

    /* ---------------------- Login ---------------------- */
    var formLogin = porId('formLogin');
    if (formLogin) {
        formLogin.addEventListener('submit', function (e) {
            limpiarErrores(formLogin);
            if (porId('username').value.trim() === '') { marcarError(porId('username'), 'Escribe tu usuario.'); }
            if (porId('password').value === '') { marcarError(porId('password'), 'Escribe tu contraseña.'); }
            if ($('.invalid', formLogin)) {
                e.preventDefault();
                enfocarPrimerError(formLogin);
            }
        });
    }
})();