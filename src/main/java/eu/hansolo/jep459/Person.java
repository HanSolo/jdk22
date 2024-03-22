package eu.hansolo.jep459;

public record Person(String firstName, String lastName, String street, String zip, String city) {
     @Override public String toString() {
         return new StringBuilder().append("{\n")
                                   .append("  \"firstName\":\"").append(firstName).append("\",\n")
                                   .append("  \"lastName\":\"").append(lastName).append("\",\n")
                                   .append("  \"street\":\"").append(street).append("\",\n")
                                   .append("  \"zip\":\"").append(zip).append("\",\n")
                                   .append("  \"city\":\"").append(city).append("\"\n")
                                   .append("}\n")
                                   .toString();
     }

     public String toJson() {
        return STR."""
               {
                 "firstName":"\{firstName}",
                 "lastName":"\{lastName}",
                 "street":""\{street}",
                 "zip":"\{zip}",
                 "city":"\{city}"
               }
               """;
     }
}
