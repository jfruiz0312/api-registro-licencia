package sv.gob.vmt.api.licencia.enums;

import java.util.List;

public record Error (String message, List<ErrorDetail> details) {}
