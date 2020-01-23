# serialization-study
These are the notes I am creating while working through the Java serialization API. For each concept I encounter, I create a Listing to address that aspect of the serialization API. Each Listing contains unit tests to help pin down the behavior of the serialization API and comments to capture the essense of what is being demonstrated.

## Contents
### Basic Listings
The basic listings demonstrate core concepts to java serialization. These ignore caveats and edge cases where possible.
- Listing 1: Serialize and deserialize with `ObjectOuputStream` and `ObjectInputStream`
  - Non-serializable classes throw NotSerializableException if serialized
- Listing 2: Serialization of fields
  - Transient fields are not serialized by default
  - Static fields are not serialized
  - Non-serializable fields throw NotSerializableException if serialized
- Listing 3: The `writeObject` and `readObject` methods control serial format
- Listing 4: Demonstrate serialization of data to a file using FileOutputStream
- TODO Listing ?: Document with `@serial`, `@serialField`, and `@serialData`
- TODO Listing ?: Causes of and how to handle serialization exceptions
### Caveat Listings
The caveat listings highlight edge cases, gotchas, and unusual behavior of the serialization API.
- TODO Listing ?: Final or transient, but never both
- TODO Listing ?: Deserialization does not invoke constructors
- TODO Listing ?: Strings are serializaed as primitives
### Advanced Listings
The advanced listings discuss aspects of the Serialization API which may be useful in specific situations.
- TODO Listing ?: Define serializable fields with `ObjectStreamField[]`
- TODO Listing ?: Externalizable?

## Best Practices
I might elide this section as its likely to be a summary of Effective Java.
