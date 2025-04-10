class UserModel {
  final int id;
  final String createdAt;
  final String name;
  final String email;
  final String password;
  final String? phone;
  final String? address;
  final String? role;
  final String? imageUrl;
  final String? userId;

  UserModel({
    required this.id,
    required this.createdAt,
    required this.name,
    required this.email,
    required this.password,
    this.phone,
    this.address,
    this.role,
    this.imageUrl,
    this.userId,
  });

  factory UserModel.fromJson(Map<String, dynamic> json) {
    return UserModel(
      id: json['id'] ?? 0,  // Default value 0
      createdAt: json['created_at'] ?? '',  // Empty string if null
      name: json['name'] ?? '',
      email: json['email'] ?? '',
      password: json['password'] ?? '',
      phone: json['phone'] ?? '',
      address: json['address'] ?? '',
      role: json['role'] ?? '',
      imageUrl: json['image_url'] ?? '',
      userId: json['user_id'] ?? '',
    );
  }

}
