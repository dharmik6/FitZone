package com.example.fitzone.diet;

public class DietItemList {
        private String name;
        private String description;
        private String imageUrl;

        public DietItemList() {
            // Empty constructor needed for Firestore
        }

        public DietItemList(String name, String description, String imageUrl) {
            this.name = name;
            this.description = description;
            this.imageUrl = imageUrl;
        }

        // Getters and setters for the properties
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

}
