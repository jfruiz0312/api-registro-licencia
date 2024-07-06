package sv.gob.vmt.api.licencia.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "LicenciaEntity")
@Table(name = "licencias")
public class LicenciaEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "no puede estar vacio")
	@Size(min = 1, max = 100, message = "el tamaño tiene que estar entre 4 y 100")
	@Column(nullable = false)
	private String nombre;

	@NotEmpty(message = "no puede estar vacio")
	@Size(min = 1, max = 100, message = "el tamaño tiene que estar entre 4 y 100")
	@Column(nullable = false)
	private String apellido;

	@NotEmpty(message = "no puede estar vacio")
	@Pattern(regexp = "^\\d{9}$", message = "El número de DUI debe contar con 9 caracteres numericos")
	private String dui;

	@NotEmpty(message = "no puede estar vacio")
	private String nroControl;

	@NotEmpty(message = "no puede estar vacio")
	@Size(min = 1, max = 20, message = "el tamaño tiene que estar entre 1 y 20")
	@Column(nullable = false)
	private String clase;

	@NotEmpty(message = "no puede estar vacio")
	private String ojos;

	@NotEmpty(message = "no puede estar vacio")
	@Column(name = "tipo_sangre")
	private String tipoSangre;
	
	private String foto;
	
	@NotNull(message = "no puede estar vacio")
	@Column(name = "fecha_expedicion")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Temporal(TemporalType.DATE)
	private Date fechaExpedicion;
	
	@NotNull(message = "no puede estar vacio")
	@Column(name = "fecha_vencimiento")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Temporal(TemporalType.DATE)
	private Date fechaVencimiento;

	@NotNull(message = "no puede estar vacio")
	@Column(name = "fecha_nacimiento")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@Temporal(TemporalType.DATE)
	private Date FechaNacimiento;	

	@NotNull(message = "no puede estar vacio")
	@Email
	private String correo;

	@NotEmpty(message = "no puede estar vacio")
	@Size(min = 1, max = 1, message = "El genero debe contar con 1 caracter")
	private String genero;


	private static final long serialVersionUID = -4482398050739307284L;

}
