# 📸 Image Guide - Zest Food Ordering System

This document specifies all image requirements for the application, including file names, locations, and resolutions.

---

## 📁 Directory Structure

All images should be placed in `src/main/resources/images/` with the following structure:

```
src/main/resources/images/
├── restaurants/          # Restaurant logos/images
│   ├── bk.png
│   ├── pizza.png
│   ├── kfc.png
│   └── default_rest.png
└── menu/                  # Menu item images
    ├── whopper.png
    ├── chicken_royale.png
    ├── fries.png
    ├── supreme.png
    ├── pepperoni.png
    ├── bucket.png
    ├── rizo.png
    └── default_item.png
```

---

## 🏪 Restaurant Images

**Location:** `src/main/resources/images/restaurants/`

### Required Images:

| File Name | Restaurant | Resolution | Aspect Ratio | Format |
|-----------|------------|------------|--------------|--------|
| `bk.png` | Burger King | **400x300px** | 4:3 | PNG |
| `pizza.png` | Pizza Hut | **400x300px** | 4:3 | PNG |
| `kfc.png` | KFC | **400x300px** | 4:3 | PNG |
| `default_rest.png` | Default/Placeholder | **400x300px** | 4:3 | PNG |

### Display Specifications:
- **Display Size:** 170x150px (scaled automatically)
- **Aspect Ratio:** 4:3 recommended
- **Format:** PNG with transparency support
- **File Size:** Keep under 200KB per image

### Design Guidelines:
- Use restaurant logos or appetizing food images
- Ensure text/logo is readable at small sizes
- Use consistent styling across all restaurant images
- Background should be clean (white or transparent)

---

## 🍔 Menu Item Images

**Location:** `src/main/resources/images/menu/`

### Required Images:

| File Name | Menu Item | Restaurant | Resolution | Aspect Ratio | Format |
|-----------|-----------|------------|------------|--------------|--------|
| `whopper.png` | Whopper | Burger King | **400x300px** | 4:3 | PNG |
| `chicken_royale.png` | Chicken Royale | Burger King | **400x300px** | 4:3 | PNG |
| `fries.png` | Fries (Medium) | Burger King | **400x300px** | 4:3 | PNG |
| `supreme.png` | Super Supreme (M) | Pizza Hut | **400x300px** | 4:3 | PNG |
| `pepperoni.png` | Pepperoni (M) | Pizza Hut | **400x300px** | 4:3 | PNG |
| `bucket.png` | Mighty Bucket | KFC | **400x300px** | 4:3 | PNG |
| `rizo.png` | Rizo | KFC | **400x300px** | 4:3 | PNG |
| `default_item.png` | Default/Placeholder | All | **400x300px** | 4:3 | PNG |

### Display Specifications:

#### In Menu Grid (Home Screen):
- **Display Size:** 200x150px
- **Aspect Ratio:** 4:3
- **Format:** PNG with transparency support
- **File Size:** Keep under 150KB per image

#### In Cart:
- **Display Size:** 80x60px (thumbnail)
- **Aspect Ratio:** 4:3
- **Format:** PNG with transparency support

### Design Guidelines:
- Use high-quality food photography
- Show the actual dish clearly
- Use consistent lighting and background
- Ensure the food is the main focus
- Avoid cluttered backgrounds
- Use professional food photography when possible

---

## 🎨 Image Specifications Summary

### Standard Resolution:
- **Recommended:** 400x300px (4:3 aspect ratio)
- **Minimum:** 300x225px
- **Maximum:** 800x600px (will be scaled down)

### File Format:
- **Format:** PNG (preferred) or JPG
- **Color Mode:** RGB
- **Transparency:** PNG with alpha channel (optional)
- **Compression:** Optimized for web (use tools like TinyPNG)

### File Size:
- **Restaurant Images:** < 200KB
- **Menu Item Images:** < 150KB
- **Total Image Assets:** ~1-2MB

---

## 📝 Image Naming Convention

### Rules:
1. Use lowercase letters only
2. Use underscores (`_`) instead of spaces
3. Keep names short and descriptive
4. Match database `image_url` values exactly
5. Use `.png` extension (or `.jpg` if needed)

### Examples:
- ✅ `chicken_royale.png`
- ✅ `super_supreme.png`
- ❌ `Chicken Royale.png` (spaces, uppercase)
- ❌ `chicken-royale.png` (hyphens)

---

## 🔧 Adding New Images

### For New Restaurants:
1. Add image to `src/main/resources/images/restaurants/`
2. Update database `restaurants` table with correct `image_url`
3. Follow naming: `restaurant_name.png` (lowercase, underscores)

### For New Menu Items:
1. Add image to `src/main/resources/images/menu/`
2. Update database `menu_items` table with correct `image_url`
3. Follow naming: `item_name.png` (lowercase, underscores)

---

## 🖼️ Image Sources & Tools

### Recommended Tools:
- **Image Editing:** GIMP, Photoshop, Canva
- **Image Optimization:** TinyPNG, ImageOptim
- **Resizing:** IrfanView, ImageMagick
- **Format Conversion:** Online converters or GIMP

### Free Image Resources:
- **Unsplash** (https://unsplash.com) - Free food photography
- **Pexels** (https://pexels.com) - Free stock photos
- **Pixabay** (https://pixabay.com) - Free images

### Tips:
- Use royalty-free images or create your own
- Ensure images are properly licensed for commercial use
- Consider hiring a photographer for professional results
- Use consistent styling across all images

---

## ✅ Image Checklist

Before deploying, ensure:

- [ ] All restaurant images are in `images/restaurants/`
- [ ] All menu item images are in `images/menu/`
- [ ] All images match database `image_url` values exactly
- [ ] All images are properly sized (400x300px recommended)
- [ ] File sizes are optimized (< 200KB each)
- [ ] Default placeholder images exist
- [ ] Images display correctly in the application
- [ ] No broken image links in console

---

## 🐛 Troubleshooting

### Image Not Displaying:
1. Check file path matches exactly (case-sensitive)
2. Verify image is in correct directory
3. Check file name matches database value
4. Ensure image file is not corrupted
5. Check console for error messages

### Image Quality Issues:
1. Use higher resolution source images
2. Avoid excessive compression
3. Use PNG for images with text/logos
4. Use JPG for photographs (if file size is concern)

### Performance Issues:
1. Optimize image file sizes
2. Use appropriate image format (PNG vs JPG)
3. Consider lazy loading for large menus
4. Cache images properly

---

*Last Updated: [Current Date]*
*Total Images Required: 11 (3 restaurants + 8 menu items)*

