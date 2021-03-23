function normalizeZeroToOne(data) {
    const normalized = [];
    const min = ss.min(data);
    const max = ss.max(data);
    data.forEach((value) => {
        const val = (value - min) / (max - min);
        normalized.push(val.toPrecision(6));
    });

    return normalized;
}
