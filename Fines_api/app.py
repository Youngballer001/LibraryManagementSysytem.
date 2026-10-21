from datetime import date
from flask import Flask, request, jsonify

app = Flask(__name__)

# Fine charged for each overdue day.
FINE_PER_DAY = 10


@app.route("/health", methods=["GET"])
def health_check():
    return jsonify({
        "status": "running",
        "service": "Library Fine Calculation API"
    })


@app.route("/calculate-fine", methods=["POST"])
def calculate_fine():
    data = request.get_json(silent=True)

    if not isinstance(data, dict):
        return jsonify({
            "error": "A JSON request body is required."
        }), 400

    due_date_text = data.get("due_date")
    return_date_text = data.get("return_date")

    if not due_date_text or not return_date_text:
        return jsonify({
            "error": "Both due_date and return_date are required."
        }), 400

    try:
        due_date = date.fromisoformat(due_date_text)
        return_date = date.fromisoformat(return_date_text)

    except (ValueError, TypeError):
        return jsonify({
            "error": "Dates must use YYYY-MM-DD format."
        }), 400

    if return_date < due_date:
        overdue_days = 0
    else:
        overdue_days = (return_date - due_date).days

    fine = overdue_days * FINE_PER_DAY

    return jsonify({
        "overdue_days": overdue_days,
        "fine_per_day": FINE_PER_DAY,
        "fine": fine,
        "currency": "KES"
    })


if __name__ == "__main__":
    app.run(
        host="127.0.0.1",
        port=5000,
        debug=False
    )