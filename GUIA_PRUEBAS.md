# 🧪 GUÍA DE PRUEBAS - SISTEMA DE CITAS

## ✅ CHECKLIST DE PRUEBAS

### 1. PRUEBA: Paciente Agenda Una Cita

#### Pasos:
1. Inicia sesión como PACIENTE (rol: PACIENTE)
2. Ve a `/paciente/menu`
3. Haz clic en **"Agendar Nueva Cita"**
4. **Verifica:**
   - ✅ Tu nombre aparece como paciente (NO puede cambiar)
   - ✅ Puedes seleccionar especialidad
   - ✅ Puedes seleccionar médico (carga dinámicamente)
   - ✅ Puedes ingresar fecha/hora
   - ✅ Puedes ingresar motivo y descripción

#### Resultado esperado:
```
✅ Formulario permite completarse
✅ Al enviar, redirige a /paciente/menu
✅ Nueva cita aparece en "Mis Citas Agendadas"
✅ Estado: "pendiente"
```

---

### 2. PRUEBA: Paciente NO puede agendar para otro

#### Pasos:
1. Como PACIENTE, intenta acceder a la URL: `/citas/nueva`
2. En el formulario, ve que tu nombre NO PUEDE CAMBIARSE
3. Intenta hacer POST directo a `/citas/guardar` con otro paciente (si sabes hacerlo)

#### Resultado esperado:
```
✅ Formulario muestra solo tu nombre (readonly)
✅ Si intentas cambiar en POST, recibes error: 
   "Solo puedes agendar citas para ti mismo."
```

---

### 3. PRUEBA: Médico ve sus citas

#### Pasos:
1. Inicia sesión como MEDICO (rol: MEDICO)
2. Ve a `/medicos/menu`
3. **Verifica:**
   - ✅ Solo ves tus citas (id_medico = tu ID)
   - ✅ Citas pendientes tienen botón "Completar Cita"
   - ✅ Citas completadas NO tienen botón de completar

#### Resultado esperado:
```
✅ Solo citas asignadas a ti aparecen
✅ Botones funcionales según estado
```

---

### 4. PRUEBA: Médico Completa Una Cita (FASE 2)

#### Pasos:
1. Como MEDICO, ve a `/medicos/menu`
2. Haz clic en **"Completar Cita"** en una cita pendiente
3. **Verifica:**
   - ✅ Información de la cita aparece (lectura)
   - ✅ Puedes ingresar Diagnóstico
   - ✅ Puedes ingresar Tratamiento
   - ✅ Puedes ingresar Observaciones
4. Ingresa datos y haz clic en **"Completar Cita"**

#### Resultado esperado:
```
✅ GET /citas/completar/{id} → Muestra formulario
✅ POST /citas/completar → Actualiza BD
✅ Redirige a /medicos/menu
✅ Cita ya NO aparece en "Pendientes"
```

---

### 5. PRUEBA: Paciente ve su cita completada

#### Pasos:
1. Como PACIENTE, ve a `/paciente/menu`
2. **Verifica:**
   - ✅ Tu cita aparece con estado "COMPLETADA"
   - ✅ Ves el diagnóstico
   - ✅ Ves el tratamiento
   - ✅ Ves las observaciones
   - ✅ NO hay botón "Completar Cita" (ese es para médico)

#### Resultado esperado:
```
✅ Estado: ✅ COMPLETADA
✅ Diagnóstico, tratamiento, observaciones visibles
✅ Información médica es de solo lectura
```

---

### 6. PRUEBA: Seguridad - Acceso no autorizado

#### 6a. Intenta acceder sin loguearte
```
GET /citas/nueva
Resultado: ❌ Redirect /login
```

#### 6b. Intenta acceder como ADMIN
```
Inicia sesión como ADMIN
GET /citas/nueva
Resultado: ❌ Redirect /login (no eres paciente)
```

#### 6c. Médico intenta ver citas de otro médico
```
Como MEDICO #1:
GET /citas/completar/{id_de_cita_medico_2}
Resultado: ❌ Redirect /medicos/menu (no es tu cita)
```

#### Resultado esperado:
```
✅ Todas las validaciones funcionan
✅ No hay acceso sin autorización
```

---

### 7. PRUEBA: Estados de cita

#### Estados posibles en BD:
```
1. "pendiente"    → Recién creada, sin completar
2. "completada"   → Médico completó, con diagnóstico/tratamiento
3. "cancelada"    → Paciente o médico canceló
```

#### Prueba cada estado:
```
Estado: pendiente
✅ Solo médico asignado puede completar
✅ Paciente puede cancelar

Estado: completada
✅ Información inmutable
✅ No hay botones de acción

Estado: cancelada
✅ No aparece en listas activas
✅ Aparece en historial
```

---

### 8. PRUEBA: Datos persisten en BD

#### Pasos:
1. Crea una cita como PACIENTE
2. Completa como MEDICO
3. Reinicia la aplicación
4. Ve a `/paciente/menu`

#### Resultado esperado:
```
✅ La cita sigue ahí con todos los datos
✅ Diagnóstico, tratamiento, observaciones se mantienen
✅ Estado sigue siendo "completada"
```

---

### 9. PRUEBA: AJAX - Carga dinámica de médicos

#### Pasos:
1. En formulario de agendar cita
2. Selecciona una especialidad
3. **Verifica:**
   - ✅ Los médicos cargan sin recargar la página
   - ✅ Solo aparecen médicos de esa especialidad
   - ✅ Si no hay médicos, muestra mensaje

#### Resultado esperado:
```
✅ AJAX funciona correctamente
✅ Médicos se filtran por especialidad
✅ Sin recargas de página
```

---

### 10. PRUEBA: Validaciones del formulario

#### Campos requeridos:
```
✅ Paciente (readonly, siempre presente)
✅ Especialidad (requerido)
✅ Médico (requerido)
✅ Fecha/Hora (requerido)
✅ Motivo (requerido)
✅ Descripción (opcional)
```

#### Intenta enviar sin campos:
```
Resultado: ❌ Error de validación HTML5
```

---

## 🐛 ERRORES COMUNES A BUSCAR

### Error 1: Import incorrecto
```
Síntoma: Error de compilación en CitaController
Causa: import javax.servlet.http.HttpSession
Solución: ✅ YA CORREGIDO
Verificar: import jakarta.servlet.http.HttpSession
```

### Error 2: No carga la cita del paciente en formulario
```
Síntoma: Selector de paciente vacío
Causa: Paciente no está en lista
Solución: Verificar que lista de pacientes se carga
```

### Error 3: Médico no puede ver sus citas
```
Síntoma: GET /medicos/menu → lista vacía
Causa: Consulta no filtra por id_medico
Solución: Verificar CitaDAOJdbc.obtenerCitasPorMedico()
```

### Error 4: Cita no se completa
```
Síntoma: POST /citas/completar → error
Causa: Campos diagnóstico/tratamiento no en BD
Solución: Verificar tabla citas tiene esos campos
```

---

## 📊 CONSULTAS SQL PARA VERIFICACIÓN

### Ver todas las citas
```sql
SELECT * FROM citas;
```

### Ver citas de un paciente
```sql
SELECT * FROM citas 
WHERE id_paciente = 1;
```

### Ver citas de un médico
```sql
SELECT * FROM citas 
WHERE id_medico = 5;
```

### Ver citas pendientes
```sql
SELECT * FROM citas 
WHERE estado = 'pendiente';
```

### Ver citas completadas
```sql
SELECT * FROM citas 
WHERE estado = 'completada';
```

### Ver cita específica con detalles
```sql
SELECT 
    c.id,
    CONCAT(p.nombre, ' ', p.apellido1) AS paciente,
    CONCAT(m.nombre, ' ', m.apellido1) AS medico,
    e.nombre AS especialidad,
    c.fecha_cita,
    c.motivo,
    c.estado,
    c.diagnostico,
    c.tratamiento,
    c.observaciones
FROM citas c
JOIN pacientes p ON c.id_paciente = p.id
JOIN medicos m ON c.id_medico = m.id
JOIN especialidades e ON m.id_especialidad = e.id
WHERE c.id = 42;
```

---

## 🔍 LOGS A BUSCAR

### En la consola de Spring Boot:

#### Cita creada exitosamente:
```
[INFO] Save cita: Cita{id=42, paciente=Paciente{id=1, nombre='Juan'}, medico=Medico{id=5}, estado='pendiente'}
```

#### Cita completada exitosamente:
```
[INFO] Update cita: Cita{id=42, estado='completada', diagnostico='...', tratamiento='...'}
```

#### Error de acceso no autorizado:
```
[WARN] Unauthorized access attempt: Usuario role = ADMIN, trying to access /citas/nueva
```

---

## 📋 CHECKLIST FINAL

- [ ] Compilación sin errores
- [ ] GET /citas/nueva → Funciona solo para pacientes logueados
- [ ] Formulario de agendar → Paciente no puede cambiarse
- [ ] POST /citas/guardar → Cita se crea en BD
- [ ] GET /medicos/menu → Solo citas del médico logueado
- [ ] GET /citas/completar/{id} → Formulario aparece
- [ ] POST /citas/completar → Cita se actualiza
- [ ] GET /paciente/menu → Cita muestra diagnóstico/tratamiento
- [ ] Seguridad: No acceso sin autenticación
- [ ] Seguridad: No acceso a citas ajenas
- [ ] BD: Datos persisten después de reiniciar
- [ ] Estilos: Colores azules consistentes
- [ ] AJAX: Carga de médicos por especialidad funciona

---

## 🚀 PRÓXIMOS PASOS

Si todas las pruebas pasan:

1. Probar en producción
2. Crear tests unitarios
3. Agregar validaciones adicionales
4. Implementar notificaciones por email
5. Agregar dashboard de admin


