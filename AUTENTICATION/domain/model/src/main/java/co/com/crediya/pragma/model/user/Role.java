package co.com.crediya.pragma.model.user;



public enum Role {
    ADMIN(1L, "ADMIN", "Usuario administrador"),
    ASESOR(2L, "ASESOR", "Usuario asesor"),
    CLIENTE(3L, "CLIENTE", "Usuario cliente"),;

    private final Long id;
    private final String name;
    private final String description;

    Role(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Role fromId(Long id) {
        for (Role role : values()) {
            if (role.getId().equals(id)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol no encontrado con ID: " + id);
    }
}
