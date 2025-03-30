const mongoose = require("mongoose");

const orderSchema = new mongoose.Schema(
  {
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "User",
      required: true,
    },
    products: [
      {
        productId: {
          type: mongoose.Schema.Types.ObjectId,
          ref: "Product",
          required: true,
        },
        quantity: { type: Number, default: 1 },
      },
    ],
    amount: { type: Number, required: true },
    address: { type: Object, required: true },
    status: {
      type: String,
      default: "pending",
      enum: ["pending", "shipped", "delivered", "cancelled"],
    },
  },
  { timestamps: true } // ✅ Automatically adds `createdAt` & `updatedAt`
);

const Order = mongoose.model("Order", orderSchema);

module.exports = Order;
