package engine;

public class Constants {

    // ============================================================
    // Fundamental Mathematical engine.Constants
    // ============================================================
    // PI defined to many digits (here provided as a String of digits)
    public static final double PI = 3.141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128481117450284102701938521105559644622948954930381964428810975665933446128475648233786783165271201909145648566923460348610454326648213393607260249141273724587006606315588174881520920962829254091715364367892590360011330530548820466521384146951941511609433057270365759591953092186117381932611793105118548074462379962749567351885752724891227938183011949129833673362440656643086021394946395224737190702179860943702770539217176293176752384674818467669405132;

    // ============================================================
    // Base Unit Definitions (as defined by modern standards)
    // ============================================================
    // 1 Second is defined as the duration of 9,192,631,770 periods
    // of the radiation corresponding to the transition between the two hyperfine levels of the ground state of cesium-133.
    public static final double SECOND = 1.0; // Base unit of time (by definition)

    // The speed of light in vacuum is defined exactly.
    public static final double SPEED_OF_LIGHT = 299792458.0; // in meters per second (m/s)

    // 1 Meter is defined as the distance light travels in vacuum in 1/299792458 seconds.
    public static final double METER = SPEED_OF_LIGHT * (SECOND / 299792458.0); // by definition, 1 meter

    // A light-second is the distance light travels in one second.
    public static final double LIGHT_SECOND = SPEED_OF_LIGHT * SECOND; // in meters

    // 1 Kilogram is defined by fixing the numerical value of the Planck constant.
    // For our purposes, we treat it as the base unit of mass.
    public static final double KILOGRAM = 1.0; // Base unit of mass

    // ============================================================
    // Electromagnetic Units (in SI, defined from fundamental constants)
    // ============================================================
    // Vacuum permittivity (ε₀) [exact by definition since 2019]
    public static final double VACUUM_PERMITTIVITY = 8.8541878128e-12; // Farads per meter (F/m)
    // Vacuum permeability (μ₀) [used to be exact, now determined experimentally]
    public static final double VACUUM_PERMEABILITY = 1.25663706212e-6; // Henry per meter (H/m)

    // ============================================================
    // Fundamental Physical engine.Constants
    // ============================================================
    // Planck constant (h) defined exactly since 2019 redefinition of SI units.
    public static final double PLANCK_CONSTANT = 6.62607015e-34; // in Joule-seconds (J·s)
    // Reduced Planck constant (ħ = h / 2π)
    public static final double REDUCED_PLANCK_CONSTANT = PLANCK_CONSTANT / (2 * PI);

    // Gravitational constant (G) [CODATA 2018 recommended value]
    public static final double GRAVITATIONAL_CONSTANT = 6.67430e-11; // in m³/(kg·s²)

    // Boltzmann constant (k) defined exactly since 2019 redefinition.
    public static final double BOLTZMANN_CONSTANT = 1.380649e-23; // in J/K

    // Avogadro constant (N_A) defined exactly.
    public static final double AVOGADRO_CONSTANT = 6.02214076e23; // in 1/mol

    // Gas constant (R), universal gas constant.
    public static final double GAS_CONSTANT = 8.314462618; // in J/(mol·K)

    // Stefan-Boltzmann constant (σ)
    public static final double STEFAN_BOLTZMANN_CONSTANT = 5.670374419e-8; // in W/(m²·K⁴)

    // ============================================================
    // Electromagnetic and Particle Properties
    // ============================================================
    // Elementary charge (e) defined exactly.
    public static final double ELEMENTARY_CHARGE = 1.602176634e-19; // in Coulombs (C)

    // Electron mass (mₑ)
    public static final double ELECTRON_MASS = 9.1093837015e-31; // in kilograms (kg)

    // Proton mass (mₚ)
    public static final double PROTON_MASS = 1.67262192369e-27; // in kilograms (kg)

    // Neutron mass (mₙ)
    public static final double NEUTRON_MASS = 1.67492749804e-27; // in kilograms (kg)

    // Fine structure constant (α), dimensionless (~1/137)
    public static final double FINE_STRUCTURE_CONSTANT = 7.2973525693e-3;

    // Rydberg constant (R_∞)
    public static final double RYDBERG_CONSTANT = 1.0973731568160e7; // in 1/m

    // Electron volt conversion: 1 eV is the energy acquired by an electron when accelerated through 1 volt.
    public static final double ELECTRON_VOLT = ELEMENTARY_CHARGE; // in Joules (J)

    // ============================================================
    // Derived Natural Units (Planck Units)
    // ============================================================
    // Planck length: l_P = sqrt(ħ G / c³)
    public static final double PLANCK_LENGTH = Math.sqrt(REDUCED_PLANCK_CONSTANT * GRAVITATIONAL_CONSTANT /
            Math.pow(SPEED_OF_LIGHT, 3)); // in meters (m)

    // Planck time: t_P = l_P / c
    public static final double PLANCK_TIME = PLANCK_LENGTH / SPEED_OF_LIGHT; // in seconds (s)

    // Planck mass: m_P = sqrt(ħ c / G)
    public static final double PLANCK_MASS = Math.sqrt(REDUCED_PLANCK_CONSTANT * SPEED_OF_LIGHT /
            GRAVITATIONAL_CONSTANT); // in kilograms (kg)

    // Planck temperature: T_P = m_P c² / k
    public static final double PLANCK_TEMPERATURE = PLANCK_MASS * Math.pow(SPEED_OF_LIGHT, 2) / BOLTZMANN_CONSTANT; // in Kelvin (K)

    // ============================================================
    // Astronomical Distance Units
    // ============================================================
    // Julian year (used for astronomical calculations): 365.25 days in seconds.
    public static final double JULIAN_YEAR = 365.25 * 24 * 3600; // in seconds (s)
    // Light-year: distance light travels in one Julian year.
    public static final double LIGHT_YEAR = SPEED_OF_LIGHT * JULIAN_YEAR; // in meters (m)
    // Astronomical Unit (AU): average distance between Earth and Sun (exact by definition)
    public static final double ASTRONOMICAL_UNIT = 149597870700.0; // in meters (m)
    // Parsec (pc): approximately 3.08567758149e16 m
    public static final double PARSEC = 3.08567758149e16; // in meters (m)

    // ============================================================
    // Common Earth & Engineering engine.Constants
    // ============================================================
    // Standard gravitational acceleration at Earth's surface.
    // (Often approximated as 9.81 m/s²; here using 9.80665 m/s² as the standard value.)
    public static final double EARTH_GRAVITY = 9.80665; // in m/s²
    // (Sometimes approximated as 9.82 m/s² in some texts.)
    public static final double EARTH_GRAVITY_APPROX = 9.82; // in m/s²

    // ============================================================
    // Unit Conversions (SI to other common units)
    // ============================================================
    // 1 inch = 0.0254 m exactly.
    public static final double INCH = 0.0254; // in meters
    // 1 foot = 12 inches.
    public static final double FOOT = 12 * INCH; // in meters
    // 1 yard = 3 feet.
    public static final double YARD = 3 * FOOT; // in meters
    // 1 mile = 5280 feet.
    public static final double MILE = 5280 * FOOT; // in meters

    // ============================================================
    // Temperature Conversions
    // ============================================================
    // Celsius to Kelvin offset.
    public static final double CELSIUS_OFFSET = 273.15; // Kelvin = Celsius + 273.15

    // ============================================================
    // Additional engine.Constants as Needed...
    // ============================================================
    // You can add many more constants here – from the classical electron radius to the mass of the Sun,
    // to the Stefan-Boltzmann constant, and so on. The above should provide a solid framework,
    // and you may expand it to include any other constants required by your application.

    // Example: Mass of the Sun (approximate)
    public static final double SOLAR_MASS = 1.98847e30; // in kilograms (kg)

    // Example: Radius of the Earth (approximate)
    public static final double EARTH_RADIUS = 6.371e6; // in meters (m)

    // Example: Gravitational acceleration at Earth's surface (using Earth's radius and mass)
    // g = G * M / R²  (this should roughly agree with EARTH_GRAVITY)
    public static final double CALCULATED_EARTH_GRAVITY = GRAVITATIONAL_CONSTANT * SOLAR_MASS / Math.pow(EARTH_RADIUS, 2);

    // ============================================================
    // Note:
    // Although many of these values are defined in SI units,
    // the base unit definitions (second, meter, kilogram) are grounded in fundamental physical constants,
    // providing a modern “from the ground up” approach.
    // ============================================================

    // ============================================================
    // Known Bodies' Masses (in kg)
    // ============================================================
    public static final double EARTH_MASS = 5.972e24;
    public static final double MOON_MASS = 7.342e22;
    public static final double SUN_MASS = 1.98847e30;
    public static final double MERCURY_MASS = 3.3011e23;
    public static final double VENUS_MASS = 4.8675e24;
    public static final double MARS_MASS = 6.4171e23;
    public static final double JUPITER_MASS = 1.8982e27;
    public static final double SATURN_MASS = 5.6834e26;
    public static final double URANUS_MASS = 8.6810e25;
    public static final double NEPTUNE_MASS = 1.02413e26;
    public static final double PLUTO_MASS = 1.303e22;
    public static final double SAGITTARIUS_A_MASS = 4.154e6 * SUN_MASS;
    public static final double ANDROMEDA_MASS = 1.5e12 * SUN_MASS;
    public static final double BETELGEUSE_MASS = 11.6 * SUN_MASS;
    public static final double SIRIUS_A_MASS = 2.063 * SUN_MASS;
    public static final double ALPHA_CENTAURI_A_MASS = 1.1 * SUN_MASS;
    public static final double ALPHA_CENTAURI_B_MASS = 0.907 * SUN_MASS;
    public static final double PROXIMA_CENTAURI_MASS = 0.1221 * SUN_MASS;
    public static final double VEGA_MASS = 2.135 * SUN_MASS;
    public static final double POLARIS_MASS = 5.4 * SUN_MASS;
}
