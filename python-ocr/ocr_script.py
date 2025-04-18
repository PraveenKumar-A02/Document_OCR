import os
from PIL import Image
import pytesseract
import json
import argparse
import cv2
import numpy as np
from googletrans import Translator

# Set Tesseract path (adjust for your system)
pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"

# Preprocessing function
def preprocess_image(image_path):
    """
    Preprocess the image to improve OCR accuracy for old, handwritten, or faded documents.
    """
    try:
        img = cv2.imread(image_path)
        gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        blurred = cv2.GaussianBlur(gray, (5, 5), 0)
        thresh = cv2.adaptiveThreshold(
            blurred, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 11, 2
        )
        kernel = np.array([[0, -1, 0], [-1, 5, -1], [0, -1, 0]], dtype=np.float32)
        sharpened = cv2.filter2D(thresh, -1, kernel)
        coords = cv2.findNonZero(sharpened)
        if coords is not None:
            angle = cv2.minAreaRect(coords)[-1]
            if angle < -45:
                angle = -(90 + angle)
            else:
                angle = -angle
            (h, w) = sharpened.shape[:2]
            center = (w // 2, h // 2)
            M = cv2.getRotationMatrix2D(center, angle, 1.0)
            sharpened = cv2.warpAffine(sharpened, M, (w, h), flags=cv2.INTER_CUBIC, borderMode=cv2.BORDER_REPLICATE)
        return Image.fromarray(sharpened)
    except Exception as e:
        print(f"Preprocessing failed: {e}")
        return Image.open(image_path)

# Tesseract OCR with preprocessing
def perform_ocr(image_path, language='eng'):
    """
    Perform OCR on the image file using Tesseract OCR with preprocessing.
    """
    try:
        processed_img = preprocess_image(image_path)
        custom_config = r'--oem 3 --psm 6'
        text = pytesseract.image_to_string(processed_img, lang=language, config=custom_config)
        return text.strip()
    except Exception as e:
        print(f"OCR failed: {e}")
        return ""

# Google Translate API
def translate_text(text, source_lang, target_lang):
    """
    Translate text using Googletrans.
    """
    try:
        # Map your language codes to Googletrans codes
        lang_map = {
            'eng': 'en',
            'tam': 'ta',
            'tel': 'te',
            'hin': 'hi',
            'jpn': 'ja'
        }
        src = lang_map.get(source_lang, 'auto')
        dest = lang_map.get(target_lang, 'en')

        translator = Translator()
        translation = translator.translate(text, src=src, dest=dest)
        return translation.text
    except Exception as e:
        print(f"Translation failed: {e}")
        return ""

def process_document(image_path, source_lang, target_lang):
    """
    Process the document, perform OCR, and translate.
    """
    original_text = perform_ocr(image_path, source_lang)
    translated_text = translate_text(original_text, source_lang, target_lang)
    return {
        'original': original_text,
        'translated': translated_text
    }

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="OCR and Translation Script")
    parser.add_argument('--image', required=True, help="Path to the image file")
    parser.add_argument('--source', required=True, help="Source language code (eng, tam, tel, hin, jpn)")
    parser.add_argument('--target', required=True, help="Target language code (eng, tam, tel, hin, jpn)")
    args = parser.parse_args()

    supported_langs = ['eng', 'tam', 'tel', 'hin', 'jpn']
    if args.source not in supported_langs or args.target not in supported_langs:
        print(json.dumps({"error": "Invalid language code. Use 'eng', 'tam', 'tel', 'hin', or 'jpn'."}))
    else:
        result = process_document(args.image, args.source, args.target)
        print(json.dumps(result))