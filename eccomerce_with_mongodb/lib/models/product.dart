import 'dart:convert';


class Product {
  final String name;
  final String description;
  final double quantity;
  final List<String> images;
  final String category;
  final double price;
  final String? id;
  Product({
    required this.name,
    required this.description,
    required this.quantity,
    required this.images,
    required this.category,
    required this.price,
    this.id,
  });

  Map<String, dynamic> toMap() {
    return {
      'name': name,
      'description': description,
      'quantity': quantity,
      'image': images,
      'category': category,
      'price': price,
      'id': id,
    };
  }

  factory Product.fromMap(Map<String, dynamic> map) {
    return Product(
      name: map['name'] ?? '',
      description: map['description'] ?? '',
      quantity: map['quantity']?.toDouble() ?? 0.0,
      images: List<String>.from(map['image']),
      category: map['category'] ?? '',
      price: map['price']?.toDouble() ?? 0.0,
      id: map['_id'],
    );
  }

  String toJson() => json.encode(toMap());

  factory Product.fromJson(String source) =>
      Product.fromMap(json.decode(source));
}



